package cz.muni.fi.car.leasing;

import java.time.LocalDate;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author Jakub Holy
 */
public class CarManagerTest {
    
    private CarManager carManager;
    private Car car;
    
    @Rule
    public final ExpectedException exception = ExpectedException.none();
    
    
    @Before
    public void setUp(){
        carManager = new CarManagerImpl();        
    }
    
    private void createNewCar(){
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
        createNewCar();
        carManager.create(car);
        Car car2 = carManager.findById(car.getId());
        assertEquals(car2,car);
        assertNotNull(car2);
    }
    
    @Test
    public void testCreateNullException(){
        exception.expect(NullPointerException.class);
        carManager.create(null);
    }
    
    @Test
    public void testDelete(){
        createNewCar();
        carManager.create(car);
        carManager.delete(car.getId());
        Car car2 = carManager.findById(car.getId());
        assertNull(car2);        
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteIllegalArgumentException(){
        carManager.delete(-3L);
    }
    
    @Test(expected = NullPointerException.class)
    public void testDeleteNullPointerException(){
        carManager.delete(null);
    }
    
    @Test
    public void testUpdate(){
        createNewCar();
        carManager.create(car);
        car.setType("Roomster");
        car.setRegistrationPlate("BRO 5674");
        car.setSeats(8);
        carManager.update(car);
        Car car2 = carManager.findById(car.getId());
        assertEquals(8, (int)car2.getSeats());
        assertEquals(car.getRegistrationPlate(),car2.getRegistrationPlate());
        assertEquals(car.getType(), car2.getType());        
    }
    
    @Test(expected = NullPointerException.class)
    public void testUpdateNullPointerException(){
        carManager.update(null);
    }
    
    @Test
    public void testFindByType(){
         createNewCar();
         carManager.create(car);
         List<Car> cars = carManager.findByType(car.getType());
         assertEquals(1, cars.size());
         assertNotNull(cars.get(0));
         assertEquals(car, cars.get(0));
    }
    
    @Test
    public void testFindAll(){
        createNewCar();
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
        assertEquals(2, cars.size());
        assertNotNull(cars.get(0));
        assertNotNull(cars.get(1));
    }
    
    @Test
    public void testFindBySeats(){
        createNewCar();
        carManager.create(car);
        List<Car> cars = carManager.findBySeats(5);
        assertEquals(1, cars.size());
        assertEquals(cars.get(0), car);
        assertNotNull(cars.get(0));
        
    }        
    
    @Test
    public void testFindByVendor(){
        createNewCar();
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
        
        assertEquals(2, cars.size());
        assertNotNull(cars.get(0));
        assertNotNull(cars.get(1));
    }
    
    @Test
    public void testFindByRegistration(){
        createNewCar();
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
        
        assertEquals(car, car3);
        assertNotNull(car);
    }
    
}
