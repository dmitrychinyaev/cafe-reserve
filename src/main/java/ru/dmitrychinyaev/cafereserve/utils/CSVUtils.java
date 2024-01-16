package ru.dmitrychinyaev.cafereserve.utils;

import com.opencsv.CSVWriter;
import org.springframework.stereotype.Component;
import ru.dmitrychinyaev.cafereserve.entity.ReservationRequest;

import java.io.FileWriter;
import java.io.IOException;

@Component
public class CSVUtils {
    public void write(ReservationRequest requestToWrite){
        try(CSVWriter writer = new CSVWriter(new FileWriter("backup.csv", true))){
            String [] arrayToWrite = requestToWrite.getDataForAdmin().split(" ");
            writer.writeNext(arrayToWrite);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
