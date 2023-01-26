package net.weg.gedesti;

import net.weg.gedesti.model.entity.Bu;
import net.weg.gedesti.model.entity.Worker;
import net.weg.gedesti.repository.WorkerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import net.weg.gedesti.repository.BuRespository;

@Component
public class DatabaseInitializer implements CommandLineRunner {
    private final WorkerRepository workerRepository;
    private final BuRespository buRepository;


    public DatabaseInitializer(WorkerRepository workerRepository, BuRespository buRepository) {
        this.workerRepository = workerRepository;
        this.buRepository = buRepository;
    }

    @Override
    public void run(String... args) {
        Worker requester = new Worker(1, "leonardo@weg.net", "Leonardo Heitor Poglia", "requester", "123");
        Worker analyst = new Worker(2, "vytor@weg.net", "Vytor Augusto Rosa", "analyst", "123");
        Worker ti = new Worker(3, "ester@weg.net", "Ester Gireli", "ti", "123");
        Worker business = new Worker(4, "eduarda@weg.net", "Eduarda Campos", "business", "123");

        Bu bu1 = new Bu(1, "WEG Motores");
        Bu bu2 = new Bu(2, "WEG Tintas");
        Bu bu3 = new Bu(3, "WEG Automação");
        Bu bu4 = new Bu(4, "WEG Energia");
        Bu bu5 = new Bu(5, "WEG Digital");
        Bu bu6 = new Bu(6, "WEG Transmissão & Distribuição");
        Bu bu7 = new Bu(7, "WEG Serviços");

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
    }
}