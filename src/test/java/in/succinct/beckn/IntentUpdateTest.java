package in.succinct.beckn;

import com.venky.geo.GeoCoordinate;
import org.junit.Test;

public class IntentUpdateTest {
    @Test
    public void intentUpdate1(){
        Intent target = new Intent();
        target.setLocation(new Location(){{
            setGps(new GeoCoordinate(12.5D,97D));
            setCircle(new Circle(){{
                setRadius(new Scalar(){{
                    setUnit("km");
                    setValue(5);
                }});
            }});
        }});
        
        Intent source = new Intent();
        source.setLocation(new Location(){{
            setCircle(new Circle(){{
                setRadius(new Scalar(){{
                    setUnit("km");
                    setValue(2);
                }});
            }});
        }});
        System.out.println(target);
        System.out.println(source);
        target.update(source,false);
        System.out.println(target);
    }
}
