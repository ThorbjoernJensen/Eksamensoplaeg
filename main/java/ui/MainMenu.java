package ui;

import domain.Pizza;
import domain.Order;
import persistence.Database;
import persistence.DbMenuCardMapper;
import persistence.DbOrderMapper;

import java.util.List;

public class MainMenu {

    private final String USER = "testdb_user";
    private final String PASSWORD = "1234";
    private final String URL = "jdbc:mysql://localhost:3306/mario?serverTimezone=CET&useSSL=false";

    private Database database = new Database(USER, PASSWORD, URL);
    private DbMenuCardMapper dbMenuCardMapper = new DbMenuCardMapper(database);
    private DbOrderMapper dbOrderMapper = new DbOrderMapper(database);

    public void mainMenuLoop() {

        boolean running = true;

        while (running) {
            showMenu();
            switch (Input.getInt("Vælg 1-8: ")) {
                case 1:
                    showMenuCard();
                    break;
                case 2:
                    showSinglePizza();
                    break;
                case 3:
                    deletePizza();
                    break;
                case 4:
                    insertPizza();
                    break;
                case 5:
                    updatePizza();
                    break;
                case 6:
                    dbOrderMapper.getAllOrders();
                    break;
                case 7:
                    insertOrder();
                    break;
                case 8:
                    running = false;
                    break;
            }
        }
        System.out.println("Tak for denne gang!");
    }

    private void showMenu() {
        System.out.println("**** Marios pizzabar - hovedmenu ******");
        System.out.println("[1] Vis menukort [2] Vis enkelt pizza [3] Fjern pizza [4] Opret ny pizza [5] Opdater pizza [6] Se alle ordre [7] Opret ny ordre [8] Afslut");
    }

    private void updatePizza() {
        System.out.println("***** Opdater pizza *******");
        int pizzaNo = Input.getInt("Indtast pizza nummer på den du vil rette: ");
        System.out.println("Indtast ny værdi, hvis den skal rettes - eller blot <retur>: ");
        Pizza pizza = dbMenuCardMapper.getPizzaById(pizzaNo);
        String newPizzaNoInput = Input.getString("Pizzanummer: (" + pizza.getPizzaNo() + "): ");
        if (newPizzaNoInput.length() > 0) {
            pizza.setPizzaNo(Integer.parseInt(newPizzaNoInput));
        }
        String newPizzaNameInput = Input.getString("Pizza navn: (" + pizza.getName() + "): ");
        if (newPizzaNameInput.length() > 0) {
            pizza.setName(newPizzaNameInput);
        }
        String newPizzaIngredientsInput = Input.getString("Pizza ingredienser: (" + pizza.getIngredients() + "): ");
        if (newPizzaIngredientsInput.length() > 0) {
            pizza.setIngredients(newPizzaIngredientsInput);
        }
        String newPizzaPriceInput = Input.getString("Pizza pris: (" + pizza.getPrice() + "): ");
        if (newPizzaPriceInput.length() > 0) {
            pizza.setPrice(Integer.parseInt(newPizzaPriceInput));
        }
        boolean result = dbMenuCardMapper.updatePizza(pizza);
        if (result) {
            System.out.println("Pizzaen med nr = " + pizzaNo + " er nu opdateret");
        } else {
            System.out.println("Vi kunne desværre ikke opdatere den nye pizza.");
        }
    }

    private void insertPizza() {
        System.out.println("**** Opret ny pizza *******");
        int pizzaNo = Input.getInt("Indtast pizza nummer: ");
        String name = Input.getString("Indtast navn på pizza: ");
        String ingredients = Input.getString("Indtast indhold: ");
        int price = Input.getInt("Indtast pris: ");
        Pizza newPizza = new Pizza(pizzaNo, name, ingredients, price);
        Pizza insertedPizza = dbMenuCardMapper.insertPizza(newPizza);
        if (insertedPizza != null) {
            System.out.println("Pizzaen med nr = " + pizzaNo + " er nu tilføjet");
            System.out.println("Pizzaen har fået DB id = " + insertedPizza.getPizzaId());
        } else {
            System.out.println("Vi kunne desværre ikke oprette den nye pizza. PizzaNo findes allerede.");
        }
    }

    private void deletePizza() {
        int pizzaNo = Input.getInt("Indtast nummer på pizza som skal fjernes: ");
        boolean result = dbMenuCardMapper.deletePizza(pizzaNo);
        if (result) {
            System.out.println("Pizzaen med nr = " + pizzaNo + " er nu fjernet");
        } else {
            System.out.println("Pizzaen med nr = " + pizzaNo + " findes ikke, og kan derfor ikke fjernes");
        }
    }

    private void showSinglePizza() {
        int pizzaNo = Input.getInt("Indtast pizzanummer: ");
        Pizza pizza = dbMenuCardMapper.getPizzaById(pizzaNo);
        if (pizza != null) {
            System.out.println("Du har fundet pizza nummer: " + pizzaNo);
            System.out.println(pizza.toString());
        } else {
            System.out.println("Pizza med nr = " + pizzaNo + " findes desværre ikke");
        }
    }

    private void showMenuCard() {
        System.out.println("**** Menukort hos Marios ******");
        List<Pizza> menuCard = dbMenuCardMapper.getAllPizzas();
        for (Pizza pizza : menuCard) {
            System.out.println(pizza.toString());
        }
    }

    private void insertOrder() {
        System.out.println("**** Opret ny ordre *******");
        int pizzaNo = Input.getInt("Indtast pizza nummer: ");
        Pizza chosenPizza = dbMenuCardMapper.getPizzaById(pizzaNo);
        if (chosenPizza == null) {
            System.out.println("Pizza med nr " + pizzaNo + " findes ikke i menuen ");
            return; // TODO Ret så hvis man skriver en pizza der ikke findes skal den fortælle det og efterfølgende give dig lov til at skrive nyt nr.
        } else {
            int amount = Input.getInt("Indtast antal: ");
            int pickup_time = Input.getInt("Hvad tid ønsker du at hente den: ");
            String custemor_name = Input.getString("Hvad er dit navn: ");
            String phone = Input.getString("Skriv telefon nr: ");
            if (chosenPizza != null) {
                Order newOrder = new Order(chosenPizza.getPizzaId(), amount, pickup_time, custemor_name, phone);
                Order insertedOrder = dbOrderMapper.insertOrder(newOrder);
                if (insertedOrder != null) {
                } else {
                    System.out.println("Vi kunne desværre ikke oprette den nye pizza. PizzaNo findes allerede.");
                }
            } else {
                System.out.println("Det lykkedes ikke at finde en pizza med det nummer");
            }
        }
    }

    private void updateOrder() {
        String orderNr =
        dbOrderMapper.updateOrder()

    }

}