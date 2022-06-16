package com.example.warresourcesapi.utils;

import com.example.warresourcesapi.model.Price;
import com.example.warresourcesapi.model.War;
import com.opencsv.CSVReader;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CSVOpener {

    public static void main(String[] args) throws IOException, InterruptedException {
        String csv = FileDownloader.downloadJSON("https://raw.githubusercontent.com/Jackhalabardnik/wars/master/INTRA-STATE_State_participants%20v5.1%20CSV.csv");
        ArrayList<String[]> arrayList = csvToArray(csv);
        assert arrayList != null;
        ArrayList<War> wars = arrayToWars(arrayList);

    }

    public static ArrayList<String[]> csvToArray(String csv) {
        try {
            StringReader stringReader = new StringReader(csv);
            CSVReader csvReader = new CSVReader(stringReader);
            ArrayList<String[]> arrayList = new ArrayList<>();
            String[] nextRecord;

            while ((nextRecord = csvReader.readNext()) != null) {
                arrayList.add(nextRecord);
            }
            return arrayList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Price> arrayToPrices(ArrayList<String[]> arrayList) {
        ArrayList<Price> prices = new ArrayList<>();
        for (int i = 1; i < arrayList.size(); i++) {
            prices.add(new Price(
                    Double.parseDouble(arrayList.get(i)[1]),
                    LocalDate.parse(arrayList.get(i)[0].substring(0, 10))
            ));
        }
        return prices;
    }

    public static ArrayList<War> arrayToWars(ArrayList<String[]> arrayList) {
        ArrayList<War> resources = new ArrayList<>();
        LocalDate startDate;
        LocalDate endDate;
        int day, month, year ;
        for (int i = 1; i < arrayList.size(); i++) {
            String[] a = arrayList.get(i);
            day = (Integer.parseInt(a[10]) == -9 ? 1 : Integer.parseInt(a[10]));
            month = (Integer.parseInt(a[9]) == -9 ? 1 : Integer.parseInt(a[9]));
            year = (Integer.parseInt(a[11]) == -9 ? 1 : Integer.parseInt(a[11]));
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
