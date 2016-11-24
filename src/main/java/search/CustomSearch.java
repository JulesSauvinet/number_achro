package search;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.search.strategy.assignments.DecisionOperator;
import org.chocosolver.solver.search.strategy.decision.Decision;
import org.chocosolver.solver.search.strategy.decision.IntDecision;
import org.chocosolver.solver.search.strategy.strategy.AbstractStrategy;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.Variable;
import org.chocosolver.util.PoolManager;

import java.util.*;

/**
 * Created by jules on 22/11/2016.
 */
public class CustomSearch extends AbstractStrategy {
    // enables to recycle decision objects (good practice)
    PoolManager<IntDecision> pool = new PoolManager();

    //org.chocosolver.solver.search.strategy.selectors.variables.FirstFail
    //        et
    //org.chocosolver.solver.search.strategy.selectors.variables.GeneralizedMinDomVarSelector

    public CustomSearch(Model model){
        super(model.getVars());
    }
    
    @Override
    public Decision getDecision() {
        IntDecision d = pool.getE();
        if(d == null) 
            d = new IntDecision(pool);
        IntVar next = null;

        Map<Integer,Integer> valuesAffected = new HashMap();
        for(Variable v : vars){
            IntVar iv = (IntVar) v;
            if(!v.isInstantiated()) {
                next = iv; break;
            }
            else{
                if (!valuesAffected.containsKey(iv.getValue()))
                    valuesAffected.put(iv.getValue(),1);
                else {
                    valuesAffected.put(iv.getValue(),valuesAffected.get(iv.getValue())+1);
                }
            }
        }
        if(next == null) {
            return null;
        }
        else {
            // next decision is assigning nextVar to its lower bound
            Set<Integer> valuesToAffect = new HashSet();
            for (int i = next.getLB();i<=next.getUB();i++){
                Integer I = i;
                if (!valuesAffected.containsKey(I))
                    valuesToAffect.add(I);
            }
            int value = next.getLB();
            if (valuesToAffect.size()>0)
                value = valuesToAffect.iterator().next();
            /*else {
                int minvalue = valuesAffected.entrySet().stream().min((entry1, entry2) -> entry1.getValue() <= entry2.getValue() ? 1 : -1).get().getKey();;
                value =minvalue;
            }*/
            //System.out.println(value);
            d.set(next,value, DecisionOperator.int_eq);
            return d;
        }
    }
}