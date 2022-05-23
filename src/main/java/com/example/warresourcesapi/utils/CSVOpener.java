package com.example.warresourcesapi.utils;

import com.example.warresourcesapi.price.Price;
import com.example.warresourcesapi.resource.Resource;
import com.example.warresourcesapi.war.War;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CSVOpener {

    public static void main(String[] args) throws IOException {
        ArrayList<String[]> lista = csvToArray("/home/janek/Downloads/","INTRA-STATE WARS v5.1 CSV.csv");
//        ArrayList<String[]> lista = csvToArray("/home/janek/Downloads/", "Conflicts participants.csv");
//        ArrayList<Resource> resources = arrayToResources(lista);
        ArrayList<War> wars = arrayToWars(lista);
        for (var el : wars) {
            System.out.println(el);
//            System.out.println(el[32]);
        }
//        var startDate = LocalDate.of(-9, -1 , 22);

    }


    public static ArrayList<String[]> csvToArray(String path, String fileName) throws IOException {
        String line = "";
        String splitBy = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        ArrayList<String[]> arrayList = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(path + fileName));
        while ((line = br.readLine()) != null) {
            arrayList.add(line.split(splitBy, -1));
        }
        return arrayList;
    }

    public static ArrayList<Price> arrayToPrices(ArrayList<String[]> arrayList) {
        ArrayList<Price> prices = new ArrayList<>();
        for (int i = 1; i < arrayList.size(); i++) {
            prices.add(new Price(
                    Double.parseDouble(arrayList.get(i)[1]),
                    formatDate(arrayList.get(i)[0])
            ));
        }
        return prices;
    }

    public static ArrayList<War> arrayToWars(ArrayList<String[]> arrayList) {
        ArrayList<War> resources = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = null;
        LocalDate endDate = null;
        int day, month, year = 0;
        for (int i = 1; i < arrayList.size(); i++) {
            String[] a = arrayList.get(i);
            day = (Integer.parseInt(a[9]) == -9 ? 1 : Integer.parseInt(a[9]));
            month = (Integer.parseInt(a[8]) == -9 ? 1 : Integer.parseInt(a[8]));
            year = (Integer.parseInt(a[10]) == -9 ? 1 : Integer.parseInt(a[10]));
            startDate = LocalDate.of(year, month, day);
            endDate = startDate.plusDays(Integer.parseInt(a[32]));
            resources.add(new War(
                    arrayList.get(i)[1],
                    startDate,
                    endDate
            ));
        }
        return resources;
    }

    public static LocalDate formatDate(String dateString) {
        String tmp = dateString.substring(1, dateString.length() - 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        return LocalDate.parse(tmp, formatter);
    }
}
