package net.weg.gedesti;

import net.weg.gedesti.model.entity.Bu;
import net.weg.gedesti.model.entity.Worker;
import net.weg.gedesti.repository.WorkerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import net.weg.gedesti.repository.BuRepository;

@Component
public class DatabaseInitializer implements CommandLineRunner {
    private final WorkerRepository workerRepository;
    private final BuRepository buRepository;


    public DatabaseInitializer(WorkerRepository workerRepository, BuRepository buRepository) {
        this.workerRepository = workerRepository;
        this.buRepository = buRepository;
    }

    @Override
    public void run(String... args) {
        Worker requester = new Worker(1, "Leonardo Heitor Poglia", "leonardo@weg.net", "123", "requester", "pt");
        Worker analyst = new Worker(2, "Vytor Augusto Rosa", "vytor@weg.net", "123", "analyst", "pt");
        Worker ti = new Worker(3, "Ester Gireli", "ester@weg.net", "123", "ti", "pt");
        Worker business = new Worker(4, "Eduarda Campos", "eduarda@weg.net", "123", "business", "pt");

        Bu bu1 = new Bu(1, "WMO-I – WEG Motores Industrial");
        Bu bu2 = new Bu(2, "WMO-C – WEG Motores Comercial");
        Bu bu3 = new Bu(3, "WEN – WEG Energia");
        Bu bu4 = new Bu(4, "WAU – WEG Automação");
        Bu bu5 = new Bu(5, "WDS – WEG Digital e Sistemas");
        Bu bu6 = new Bu(6, "WDC – WEG Drives e Controls");
        Bu bu7 = new Bu(7, "WTI – WEG Tintas");
        Bu bu8 = new Bu(8, "WTD – WEG Transmissão e Distribuição");



        workerRepository.save(requester);
        workerRepository.save(analyst);
        workerRepository.save(ti);
        workerRepository.save(business);

        buRepository.save(bu1);
        buRepository.save(bu2);
        buRepository.save(bu3);
        buRepository.save(bu4);
        buRepository.save(bu5);
        buRepository.save(bu6);
        buRepository.save(bu7);
        buRepository.save(bu8);
    }
}