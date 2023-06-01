package net.weg.gedesti;

import net.weg.gedesti.model.entity.Bu;
import net.weg.gedesti.model.entity.Commission;
import net.weg.gedesti.model.entity.Worker;
import net.weg.gedesti.repository.CommissionRepository;
import net.weg.gedesti.repository.WorkerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import net.weg.gedesti.repository.BuRepository;

@Component
public class DatabaseInitializer implements CommandLineRunner {
    private final WorkerRepository workerRepository;
    private final BuRepository buRepository;
    private final CommissionRepository commissionRepository;


    public DatabaseInitializer(WorkerRepository workerRepository, BuRepository buRepository, CommissionRepository commissionRepository) {
        this.workerRepository = workerRepository;
        this.buRepository = buRepository;
        this.commissionRepository = commissionRepository;
    }

    @Override
    public void run(String... args) {
        Worker requester = new Worker(1, "Leonardo Heitor Poglia", "leonardo@weg.net", "123", "requester", "pt", "machining", false, false);
        Worker analyst = new Worker(2, "Vytor Augusto Rosa", "vytor@weg.net", "123", "analyst", "pt", "ti", false, false);
        Worker ti = new Worker(3, "Ester Girelli", "ester@weg.net", "123", "ti", "pt", "rh", false, false);
        Worker business = new Worker(4, "Eduarda Campos", "eduarda@weg.net", "123", "business", "pt", "ti", false, false);

        Bu bu1 = new Bu(1, "WMO-I – WEG Motores Industrial");
        Bu bu2 = new Bu(2, "WMO-C – WEG Motores Comercial");
        Bu bu3 = new Bu(3, "WEN – WEG Energia");
        Bu bu4 = new Bu(4, "WAU – WEG Automação");
        Bu bu5 = new Bu(5, "WDS – WEG Digital e Sistemas");
        Bu bu6 = new Bu(6, "WDC – WEG Drives e Controls");
        Bu bu7 = new Bu(7, "WTI – WEG Tintas");
        Bu bu8 = new Bu(8, "WTD – WEG Transmissão e Distribuição");

        Commission commission1 = new Commission(1, "CPVM – Comissão de Processos de Vendas e Desenvolvimento de produtos", "CPVM");
        Commission commission2 = new Commission(2, "CPGCI – Comissão de Processos da Cadeia Integrada", "CPGCI");
        Commission commission3 = new Commission(3, "CPGPR – Comissão de Processos de Gestão de Projetos", "CPGPR");
        Commission commission4 = new Commission(4, "CGPN – Comitê de Gestão de Processos de Negócio", "CGPN");
        Commission commission5 = new Commission(5, "CTI – Comitê de TI", "CTI");
        Commission commission6 = new Commission(6, "CWBS – Comitê WEG Business Services", "CWBS");
        Commission commission7 = new Commission(7, "DTI – Diretoria de TI", "DTI");


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

        commissionRepository.save(commission1);
        commissionRepository.save(commission2);
        commissionRepository.save(commission3);
        commissionRepository.save(commission4);
        commissionRepository.save(commission5);
        commissionRepository.save(commission6);
        commissionRepository.save(commission7);
    }
}