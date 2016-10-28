package com.nago.dataanalyzer.logic;
import java.util.List;

public class Correlation {

        private static float mean(List<Float> numbers, List<Float> corresponding) {
            float sum = 0;
            float countable = 0;
            for(Float n: numbers) {
                if(n != null && corresponding.get(numbers.indexOf(n)) != null) {
                    sum += n;
                    countable++;
                } else {}
            }
            return sum/countable;
        }

        private static float standardDeviation(List<Float> numbers, List<Float> corresponding) {
            float x = 0;
            float countable = 0;
            for(Float n: numbers) {
                if(n != null && corresponding.get(numbers.indexOf(n)) != null) {
                    x += ((n - mean(numbers,corresponding)) * (n - mean(numbers,corresponding)));
                    countable++;
                } else {}
            }
            float y = x/(countable-1);
            return (float) Math.sqrt(y);
        }

        private static float numberOfNulls(List<Float> a) {
            float x = 0;
            for(Float d: a) {
                if(d == null) {
                    x++;
                }
            }
            return x;
        }

        public static float CorrelationCoefficient(List<Float> a, List<Float> b) {
            assert a.size() == b.size();
            List<Float> mostNull;
            if(numberOfNulls(a)<=numberOfNulls(b)) { mostNull = b; } else { mostNull = a; }
            float x = 0;
            for(int i = 0; i<a.size(); i++) {
                if(a.get(i) == null || b.get(i) == null){} else {
                    float y = (a.get(i) - mean(a,b)) / standardDeviation(a,b);
                    float z = (b.get(i) - mean(b,a)) / standardDeviation(b,a);
                    x += y * z;
                }
            }
            return x/(mostNull.size()-numberOfNulls(mostNull)-1);
        }
}
