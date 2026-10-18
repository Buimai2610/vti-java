import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.params.ParameterizedClass;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;

//@TestMethodOrder(value = MethodOrderer.class )

//@ParameterizedClass
//@CsvSource({"apple, 1$", "orange, 2$"})
public class TestCaculate {
    /* */
    static  Logger logger = LogManager.getLogger(TestCaculate.class);
    static  caculate caculate;
    String friut, price;
//    TestCaculate(String fruit, String price){
//        this.friut = fruit;
//        this.price = price;
//    }


    @BeforeAll
    /* run before any test case  CHạy trước khi chạy TC
    * chỉ chạy trong class */
    static  void beforeAll() {
        caculate = new caculate();
        logger.debug("Intialize Caculator object ", caculate);
    }
    @AfterAll
    // run affer last TC
    // run only one
    static  void afterAll(){
        caculate = null;
        logger.debug("remove caculator object {}",caculate);
    }
    @AfterAll
    static  void afterAll_1(){
        caculate = null;
        logger.debug("r2  {}",caculate);
    }
    @Test
    void testAdd(){
        int result = new caculate().add(0, 3);
        Assertions.assertEquals(2, result);
    }
    @Test
    void testMinus(){
        int result =  caculate.minus(0, 3);
        Assertions.assertEquals(-3, result);

    }

    @BeforeEach
    //run before every YC
    void beforeEach(TestInfo testInfo){
        logger.info("Start Testing case {}--- ", testInfo.getTestMethod());
    }

    @AfterEach
        //run before every YC
    void afferEach(TestInfo testInfo){
        logger.info("Start Testing case {} ", testInfo.getTestMethod());
    }


    @ParameterizedTest
    @ValueSource (ints = {-2, 0,2,4,6})
    void testAddFuntionWithZero(int arg ){
        int result = caculate.add(0,arg );
       // Assertions.assertEquals(arg, result);
        Assumptions.assumeTrue(result > 0 );
        logger.info("Add function works correctly when adding a with()", arg, result);
    }

 // beforall -> beforEach -> @Test || @ParameterizedTest -> @AfterEach -> AfterAll
 @ParameterizedTest
 @ValueSource (classes = caculate.class)
 void testAddFuntionWithZero(Class<?> arg ){
     int result = caculate.add(0,2 );
     Assertions.assertEquals(arg, result);
     logger.info("Add function works correctly when adding a with()", arg, result);
 }
 @ParameterizedTest
    @EnumSource (names = {"ADD", "MULTIPLY", "MINUS"})
    public  void testEnumSorcu(MathOperator arg){
    Assertions.assertTrue(arg ==MathOperator.ADD || arg ==MathOperator.MINUS);
 }

    @ParameterizedTest
    @NullSource // add exxtra null data
    @EmptySource// ad extra empty data
    @ValueSource (strings  = {"-2", "0" ,"6"})
    void testAddFuntionWithZeroUsingString(String arg ){
        int result = caculate.add(0,Integer.parseInt(arg) );
        Assertions.assertEquals(Integer.parseInt(arg), result);
        logger.info("Add function works correctly when adding a with()", arg, result);
    }


    @ParameterizedTest
    // best used for key - value pair
    @CsvSource (value = {
            "fruit name, discount price, fixed price ",
            "apple, 1$, 3$",
            "orange, 2$, 5$",
            "cucumber, 1$, 6$"
    },useHeadersInDisplayName = true )
    void testCsvsource (String fruit, String discountPrice, String fixedPrice){
        assertFalse(fruit.isEmpty());
        // validatte sell price ( fixed - discount)  > 0
        int fixed = Integer.parseInt(fixedPrice.replaceAll("\\$", ""));
        int discount = Integer.parseInt(discountPrice.replaceAll("\\$", ""));
        Assertions.assertTrue(caculate.minus(fixed,discount)>=0 );
    }



    @ParameterizedTest
    // best used for key - value pair
    @CsvSource (value = {
            "fruit name | discount price | fixed price ",
            "apple      | 1              | 3",
            "orange     | 2              | 5",
            "cucumber   | 1              | 6"
    },useHeadersInDisplayName = true, ignoreLeadingAndTrailingWhitespace = true, delimiter = '|')
    void testCsvsource_1(String fruit, int discountPrice, int fixedPrice){
        assertFalse(fruit.isEmpty());
        // validatte sell price ( fixed - discount)  > 0
        Assertions.assertTrue(caculate.minus(fixedPrice,discountPrice)>=0 );
    }
// Đang lỗi
    @ParameterizedTest
    // best used for key - value pair
    @CsvFileSource ( resources = "/fruid.csv",
            numLinesToSkip = 1,
            ignoreLeadingAndTrailingWhitespace = true)
    void testCsvsFileource_1(String fruit, int discountPrice, int fixedPrice){
        assertFalse(fruit.isEmpty());
        // validatte sell price ( fixed - discount)  > 0
        Assertions.assertTrue(caculate.minus(fixedPrice,discountPrice)>=0 );
    }

    @ParameterizedTest
    @MethodSource(value = "student")
    void  testWithMethodSource (String student){
        assertFalse(student.isEmpty());
    }

    @ParameterizedTest
    @ArgumentsSource(value = MyDataProvider.class)
    void testWithArgumenSource(int number ){
 //   assertFalse(fruit.isEmpty());
        assertTrue(number > 0 );
    }


    @ParameterizedTest
    @ArgumentsSource(value = MyDataProvider.class)
    @MethodSource(value = "student")
    void testMultipleSource(String  value ){
        assertFalse(value.isEmpty() );
    }


    @ParameterizedTest
    @ValueSource (ints = {-2, 0,2,4,6})
    @Tag("sanyti")
    void testValueSource(int arg ){
        Assumptions.assumingThat(arg > 0,
                () -> {
            Assumptions.assumeTrue(arg >  0);
                });
        Assumptions.assumingThat(arg <- 0, ()
                -> {
            Assumptions.assumeTrue(arg <=  0);
        });

       // logger.info("Add function works correctly when adding a with()", arg);
    }

    @ParameterizedTest(name = "{index}-{displayName}")
    @ValueSource(ints= {-2, 0,2,4,6,8})
    @DisplayName("testing soft Assert ")
   // @Order(2)

    void testSortAssert(int arg, TestInfo info ) {
        logger.info("Start TC {} ", info.getDisplayName());
        Assertions.assertAll (
                ()->{assertTrue(arg < 0, "This number" + arg + "is not negative ");} ,
                ()->{assertEquals( 0, arg, "This number" + arg + "is not Zero ");},
                ()->{assertTrue(arg > 0, "This number" + arg + "is not negative ");}
        );

    }

    @Test
    @Tag("Smoke") //Đặt tên tag cho hàm test -> Để có thể gọi chạy test cùng tag với nhau
   // @Disabled //TC sẽ bị disable không được run
    @EnabledIfSystemProperty(named = "time", matches = "night")
   // @Order(1)
    void testConditionExe(){
        Assertions.assertTrue(true);
        System.out.println("Test is exe");
    }

}
