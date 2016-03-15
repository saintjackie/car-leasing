package cz.muni.fi.car.leasing;

import java.time.LocalDate;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author Jakub Holy
 */
public class CarManagerImplTest {
    
    private CarManager carManager;
    private Car car;
    
    @Rule
    public final ExpectedException exception = ExpectedException.none();
    
    
    @Before
    public void setUp(){
        carManager = new CarManagerImpl();
        car = new Car();
        car.setId(1L);
        car.setType("Octavia");
        car.setVendor("Skoda");
        car.setModelYear(LocalDate.now());
        car.setRegistrationPlate("BKE 5435");
        car.setSeats(5);
    }
    
    @Test
    public void testCreate(){
        carManager.create(car);
        Car car2 = carManager.findById(car.getId());
        Assert.assertEquals(car2,car);
    }
    
    @Test
    public void testCreateNullException(){
        exception.expect(NullPointerException.class);
        carManager.create(null);
    }
    
    @Test
    public void testDelete(){
        carManager.create(car);
        carManager.delete(car.getId());
        Car car2 = carManager.findById(car.getId());
        Assert.assertNull(car2);        
    }
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteIllegalArgumentException(){
        carManager.delete(-3L);
    }
    
    @Test
    public void testUpdate(){
        carManager.create(car);
        car.setType("Roomster");
        car.setRegistrationPlate("BRO 5674");
        car.setSeats(8);
        carManager.update(car);
        Car car2 = carManager.findById(car.getId());
        Assert.assertEquals(8, (int)car2.getSeats());
        Assert.assertEquals(car.getRegistrationPlate(),car2.getRegistrationPlate());
        Assert.assertEquals(car.getType(), car2.getType());
    }
    
    @Test
    public void testFindByType(){
         carManager.create(car);
         List<Car> cars = carManager.findByType(car.getType());
         Assert.assertEquals(1, cars.size());
         Assert.assertEquals(car, cars.get(0));
    }
    
    @Test
    public void testFindAll(){
        carManager.create(car);
        Car car2 = new Car();
        car2.setId(2L);
        car2.setModelYear(LocalDate.now());
        car2.setType("X6");
        car2.setVendor("BMW");
        car2.setSeats(5);
        car2.setRegistrationPlate("LOL 0001");
        carManager.create(car2);
        List<Car> cars = carManager.findAll();
        Assert.assertEquals(2, cars.size());
    }
    
    @Test
    public void testFindBySeats(){
        carManager.create(car);
        List<Car> cars = carManager.findBySeats(5);
        Assert.assertEquals(1, cars.size());
    }        
    
    @Test
    public void testFindByVendor(){
        carManager.create(car);
        Car car2 = new Car();
        car2.setId(2L);
        car2.setModelYear(LocalDate.now());
        car2.setType("Roomster");
        car2.setVendor("Skoda");
        car2.setSeats(5);
        car2.setRegistrationPlate("POP 1234");
        carManager.create(car2);
        
        List<Car> cars = carManager.findByVendor("Skoda");
        
        Assert.assertEquals(2, cars.size());        
    }
    
    @Test
    public void testFindByRegistration(){
        carManager.create(car);
        Car car2 = new Car();
        car2.setId(2L);
        car2.setModelYear(LocalDate.now());
        car2.setType("Roomster");
        car2.setVendor("Skoda");
        car2.setSeats(5);
        car2.setRegistrationPlate("POP 1234");
        carManager.create(car2);
        
        Car car3 = carManager.findByRegistration(car.getRegistrationPlate());
        
        Assert.assertEquals(car, car3);
        
    }
    
}
