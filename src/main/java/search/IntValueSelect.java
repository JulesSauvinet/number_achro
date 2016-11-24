package search;

import org.chocosolver.solver.search.strategy.selectors.values.IntValueSelector;
import org.chocosolver.solver.variables.IntVar;

import java.util.*;

/**
 * Created by jules on 23/11/2016.
 */
public class IntValueSelect implements IntValueSelector {
    IntVar[] vars;
    
    public IntValueSelect(IntVar[] vars){
        this.vars = vars;
    }
    
    @Override
    public int selectValue(IntVar intVar) {
        Map<Integer,Integer> valuesAffected = new HashMap();
        List<Integer> valuesToAffect = new ArrayList<>();

        for(IntVar v : vars){
            if (v.isInstantiated()) {
                if (!valuesAffected.containsKey(v.getValue()))
                    valuesAffected.put(v.getValue(), 1);
                else {
                    valuesAffected.put(v.getValue(), valuesAffected.get(v.getValue()) + 1);
                }
            }
        }
        for (int i = intVar.getLB();i<=intVar.getUB();i++){
            Integer I = i;
            if (!valuesAffected.containsKey(I))
                valuesToAffect.add(I);
        }
        int value = intVar.getLB();
        if (valuesToAffect.size()>0) {
            Collections.shuffle(valuesToAffect);
            value = valuesToAffect.iterator().next();
        }
        else {
            int minvalue = valuesAffected.entrySet().stream().min((entry1, entry2) -> entry1.getValue() <= entry2.getValue() ? 1 : -1).get().getKey();
            List<Integer> keys = new ArrayList(valuesAffected.keySet());
            Collections.shuffle(keys);
            for (Integer o : keys) {
                if (valuesAffected.get(o) <= valuesAffected.get(minvalue)){
                    minvalue=o;
                }
            }

            value =minvalue;
        }
        return value;
    }
}
