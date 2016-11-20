# Syntax : script.sh <input_dir> <output_dir>

# Create output dir
if [ ! -d $2 ]; then
	mkdir $2
else
	rm $2/*
fi

# Iterate over input files
for entry in "$1"/*
do
	nbLines=$(wc -l < $entry)
	if [ $nbLines -lt 600 ]; then
		echo "$entry		($nbLines)"
		IFS='/' read -ra filepath <<< "$entry"
		filename="$2/${filepath[1]}"
		while IFS='' read -r line || [[ -n "$line" ]]; do
		
			firstChar="$(echo $line | head -c 1)"
			if [ $firstChar = "p" ]; then
				
				IFS=' ' read -ra ADDR <<< "$line"
				nbVertices="${ADDR[2]}"
				nbEdges="${ADDR[3]}"
				
				# Print nb vertexes
				echo "$nbVertices" > "$filename"
				
				# Print vertexes
				for i in $(seq 1 $((nbVertices))); do
					echo "v$i" >> "$filename"
				done
				
				# Print nb edges
				echo "$nbEdges" >> "$filename"
			elif [ $firstChar = "e" ]; then
				IFS=' ' read -ra ADDR <<< "$line"
				v1="${ADDR[1]}"
				v2="${ADDR[2]}"
				
				# Print edges
				echo "v$v1 v$v2" >> "$filename"
			fi
		done < "$entry"

	fi
done
