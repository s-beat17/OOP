package com.company;

import org.junit.*;

public class Lab9Test {

    @Test
    public void testisEmpty() {
        Assert.assertTrue(AddDialogProd.init((String) null));
        Assert.assertTrue(AddDialogProd.init(""));
    }
    /** Данный тест проверяет метод isEmpty на корректную обработку непустых значений
     * ( возвращает «ложь» при получении строки, содержащей хотя бы один символ.
     **/

    @Test
    public void testNonisEmpty() {
        Assert.assertFalse(AddDialogProd.init(" "));
        Assert.assertFalse(AddDialogProd.init("BMW"));
    }

    @Test(expected = RuntimeException.class) // Проверяем на появление исключения
    public void testException() {
        throw new RuntimeException("Ошибка");
    }
    @BeforeClass // Фиксируем начало тестирования
    public static void allTestsStarted() {
        System.out.println("Начало тестирования");
    }
    @AfterClass // Фиксируем конец тестирования
    public static void allTestsFinished() {
        System.out.println("Конец тестирования");
    }
    @Before // Фиксируем запуск теста
    public void testStarted() {
        System.out.println("Запуск теста");
    }
    @After // Фиксируем завершение теста
    public void testFinished() {
        System.out.println("Завершение теста");
    }
}