package reco.challenge.function;

import org.apache.spark.api.java.function.Function;
import scala.Tuple2;

import java.util.*;

public class TupleListConverter implements Function<Tuple2<String, Iterable<String>>, List<String>> {
    public List<String> call(Tuple2<String, Iterable<String>> s) {

        Iterator it = s._2.iterator();
        List<String> retVal = new ArrayList<>();

        List<Integer> tmpArr = new ArrayList<>();

        Integer first = 0;
        Integer second = 0 ;

        //convert iterator to list
        while (it.hasNext()){
            tmpArr.add(Integer.parseInt((String)it.next()));
        }

        tmpArr = new ArrayList<>(new HashSet<>(tmpArr));

        for (Integer i=0; i < tmpArr.size(); i++){
            first = tmpArr.get(i);

            for (Integer j=0; j < tmpArr.size(); j++){
                if (j+i < tmpArr.size()) {
                    second = tmpArr.get(j + i);
                }
                else {
                    continue;
                }

                if (first != second) {
                    if (first != 0 && second != 0){

                        //sort
                        if (first < second) {
                            retVal.add(first + "," + second);
                        }
                        else {
                            retVal.add(second + "," + first);
                        }
                    }
                }
            }
        }
        retVal = new ArrayList<>(new HashSet<>(retVal));
        return retVal;
    }
}
