package com.nago.dataanalyzer.prompt;

import com.nago.dataanalyzer.logic.Analyzer;
import com.nago.dataanalyzer.logic.Correlation;
import com.nago.dataanalyzer.model.Country;
import com.nago.dataanalyzer.model.Country.CountryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Prompter {
    private static Analyzer analyzer = new Analyzer();
    private Scanner scanner = new Scanner(System.in);
    private boolean exit;

    public void runApplication() {
        do {
            promptMainMenu();
        } while (!exit);
    }

    private void message(String message, Object... objects) {
        System.out.printf(message, objects);
    }

    private void promptMainMenu() {
        message("%n0----||===============================>%n" +
                "To chose enter a NUMBER of an option.%n" +
                "1. View list of Countries %n" +
                "2. Indicator statistics %n" +
                "3. Edit Country.%n" +
                "4. Add Country %n" +
                "5. Remove Country %n" +
                "6.Exit%n0----||===============================>%n");
        checkForDigit();
        switch (scanner.nextInt()) {
            case 1:
                promptListOfCountries();
                break;
            case 2:
                promptIndicatorStatistics();
                break;
            case 3:
                promptToEditCountry();
                break;
            case 4:
                promptToAddCountry();
                break;
            case 5:
                promptToRemoveCountry();
                break;
            case 6:
                analyzer.closeFactory();
                exit = true;
                break;
            default:
                message("There is NO such an option");
                break;
        }
    }

    private void promptListOfCountries() {
        List<Country> countries = analyzer.fetchAllContacts();
        String leftAlignFormat = "| %-4s | %-30s | %-14s | %-8s |%n";

        System.out.format("+------+--------------------------------+----------------+----------+%n");
        System.out.format("| Code | Country                        | Internet Users | Literacy |%n");
        System.out.format("+------+--------------------------------+----------------+----------+%n");
        for (Country c : countries) {

            System.out.format(
                    leftAlignFormat,
                    c.getCode(),
                    c.getName(),
                    c.getInternetUsers() == null ? "--" : String.format("%.2f", c.getInternetUsers()),
                    c.getAdultLiteracyRate() == null ? "--" : String.format("%.2f", c.getAdultLiteracyRate()));
        }
        System.out.format("+------+--------------------------------+----------------+----------+%n");
    }

    private void promptIndicatorStatistics() {
        correlationCoefficient();
        internetUserIndicator();
        adultLiteracyRateIndicator();
    }

    private void correlationCoefficient( ) {
        List<Float>literacyRates = new ArrayList<>();
        List<Float>internetUsage = new ArrayList<>();
        analyzer.fetchAllContacts().forEach(country -> literacyRates.add(country.getAdultLiteracyRate()));
        analyzer.fetchAllContacts().forEach(country -> internetUsage.add(country.getInternetUsers()));
     message("Correlation Coefficient = %.6f%n", Correlation.CorrelationCoefficient(literacyRates, internetUsage));
    }

    private void adultLiteracyRateIndicator() {
        ArrayList<Country> countries = (ArrayList<Country>) analyzer.fetchAllContacts();

        List<Country> maxAdultLiteracyRate = new ArrayList<>();
        List<Country> minAdultLiteracyRate = new ArrayList<>();

        final float[] max = {Float.MIN_VALUE};
        final float[] min = {Float.MAX_VALUE};

        countries.stream()
                .filter(c -> c.getAdultLiteracyRate() != null && c.getAdultLiteracyRate() > max[0])
                .forEach(c -> max[0] = c.getAdultLiteracyRate());

        maxAdultLiteracyRate.addAll(countries.stream()
                .filter(c -> c.getAdultLiteracyRate() != null && Objects.equals(c.getAdultLiteracyRate(), max[0]))
                .collect(Collectors.toList()));

        countries.stream()
                .filter(c -> c.getAdultLiteracyRate() != null && c.getAdultLiteracyRate() < min[0])
                .forEach(c -> min[0] = c.getAdultLiteracyRate());

        minAdultLiteracyRate.addAll(countries.stream()
                .filter(c -> c.getAdultLiteracyRate() != null && c.getAdultLiteracyRate() == min[0])
                .collect(Collectors.toList()));
        message("Max adult literacy rate country:%n%s%n%nMin adult literacy rate country: %n%s%n%n",
                maxAdultLiteracyRate.toString().replace("[", "").replace("]", "").replace("{", " "). replace("}", ""),
                minAdultLiteracyRate.toString().replace("[", "").replace("]", "").replace("{", " "). replace("}", ""));
    }

    private void internetUserIndicator() {
        ArrayList<Country> countries = (ArrayList<Country>) analyzer.fetchAllContacts();

        List<Country> maxInternetUserCountry = new ArrayList<>();
        List<Country> minInternetUserCountry = new ArrayList<>();

        final float[] max = {Float.MIN_VALUE};
        final float[] min = {Float.MAX_VALUE};

        countries.stream()
                .filter(c -> c.getInternetUsers() != null && c.getInternetUsers() > max[0])
                .forEach(c -> max[0] = c.getInternetUsers());

        maxInternetUserCountry.addAll(countries.stream()
                .filter(c -> c.getInternetUsers() != null && Objects.equals(c.getInternetUsers(), max[0]))
                .collect(Collectors.toList()));

        countries.stream()
                .filter(c -> c.getInternetUsers() != null && c.getInternetUsers() < min[0])
                .forEach(c -> min[0] = c.getInternetUsers());

        minInternetUserCountry.addAll(countries.stream()
                .filter(c -> c.getInternetUsers() != null && c.getInternetUsers() == min[0])
                .collect(Collectors.toList()));
        message("Max internet user country:%n%s%n%nMin internet user country: %n%s%n%n",
                maxInternetUserCountry.toString().replace("[", "").replace("]", "").replace("{", " "). replace("}", ""),
                minInternetUserCountry.toString().replace("[", "").replace("]", "").replace("{", " "). replace("}", ""));
    }

    private void promptToEditCountry() {
        String input;
        Country country = null;
        boolean matching = false;
        scanner.nextLine();
        do {
            message("Enter Country name to edit:%n");
            input = notEmptyInput();
            for (Country countryFromList : analyzer.fetchAllContacts()) {
                if (input.equalsIgnoreCase(countryFromList.getName())) {
                    matching = true;
                    country = countryFromList;
                    break;
                }
            }
            if (!matching) {
                message("No such a country. Try again:%n");
            }
        } while (!matching);
        promptEditMenu(country);

    }

    private void promptEditMenu(Country country){
        message("%n0----||===============================>%n" +
                "To chose enter a NUMBER of an option to edit %s.%n" +
                "1. Code %n" +
                "2. Name %n" +
                "3. Internet Users%n" +
                "4. Adult Literacy Rate%n" +
                "0----||===============================>%n", country.getName());
        checkForDigit();

        switch (scanner.nextInt()) {
            case 1:
                message("Current country code - %s%nEnter new code:%n", country.getCode());
                scanner.nextLine();
                String code = correctCode().toUpperCase();
                Country country1 = new CountryBuilder(code, country.getName())
                        .withInternetUsers(country.getInternetUsers())
                        .withAdultLiteracyRate(country.getAdultLiteracyRate())
                        .build();
                analyzer.delete(country);
                analyzer.save(country1);
                message("Country code changed to %s successfully!%n", country1.getCode());
                break;
            case 2:
                message("Current country name - %s%nEnter new name:%n", country.getName());
                scanner.nextLine();
                country.setName(notEmptyInput());
                analyzer.update(country);
                message("Country name changed to %s successfully!%n", country.getName());
                break;
            case 3:
                message("Current internet users indicator value - %f%nEnter new value:%n", country.getInternetUsers());
                scanner.nextLine();
                Float internetUsers = correctValue();
                country.setInternetUsers(internetUsers);
                analyzer.update(country);
                message("Internet users indicator changed to %f successfully!%n", country.getInternetUsers());
                break;
            case 4:
                message("Current adult literacy rate - %f%nEnter new value:%n", country.getAdultLiteracyRate());
                scanner.nextLine();
                Float adultLiteracyRate =correctValue();
                country.setAdultLiteracyRate(adultLiteracyRate);
                analyzer.update(country);
                message("Adult literacy rate changed to %f successfully!%n", country.getAdultLiteracyRate());
                break;
        }
    }

    private void promptToAddCountry() {
        scanner.nextLine();
        message("Enter Country code:%n");
        String code = correctCode().toUpperCase();

        message("Enter Country name:%n");
        String name = notEmptyInput();

        message("Enter Country internet users indicator:%n");
        Float internetUsers = correctValue();

        message("Enter Country adult literacy rate:%n");
        Float adultLiteracyRate = correctValue();

        Country country = new CountryBuilder(code, name)
                .withInternetUsers(internetUsers)
                .withAdultLiteracyRate(adultLiteracyRate)
                .build();

        analyzer.save(country);
        message("The country %s was added successfully!%n", country.getName());
    }

    private void promptToRemoveCountry() {
        String country;
        boolean matching = false;
        scanner.nextLine();
        do {
            message("Enter name of the Country to remove:%n");
            country = notEmptyInput();
            for (Country countryFromList : analyzer.fetchAllContacts()) {
                if (country.equalsIgnoreCase(countryFromList.getName())) {
                    matching = true;
                    analyzer.delete(countryFromList);
                    message("The country %s was removed successfully!%n", countryFromList.getName());
                    break;
                }
            }
            if (!matching) {
                message("No such a country. Try again:%n");
            }
        } while (!matching);
    }

    private String correctCode(){
        String code;
        do{
            code = notEmptyInput().toUpperCase();
            if(code.length()!=3){
                message("Code should be 3 characters long%nPlease try again:%n");
            }
        }while (code.length()!=3);
        return code;
    }

    private Float correctValue(){
        Float value;
        do{
            checkForDigit();
            value = Float.parseFloat(notEmptyInput());
            if(value<0 || value>100){
                message("Value has to be in range from 0 to 100%nPlease try again:%n");
            }
        }while (value<0 || value>100);
        if(value==0){value=null;}
        return value;
    }

    private void checkForDigit() {
        while (!scanner.hasNextFloat()) {
            message("That is NOT a number, please try again:%n");
            scanner.next();
        }
    }

    private String notEmptyInput() {
        String input;
        do {
            input = scanner.nextLine();
            if (input.isEmpty() || input.matches("\\s+")) {
                message("Can't be empty. Try again:%n");
            }
        }
        while (input.isEmpty() || input.matches("\\s+"));
        return input;
    }
}
