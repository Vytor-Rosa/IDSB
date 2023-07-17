package net.weg.gedesti;

import net.weg.gedesti.model.entity.*;
import net.weg.gedesti.repository.ColorsRepository;
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
    private final ColorsRepository colorsRepository;


    public DatabaseInitializer(WorkerRepository workerRepository, BuRepository buRepository, CommissionRepository commissionRepository, ColorsRepository colorsRepository) {
        this.workerRepository = workerRepository;
        this.buRepository = buRepository;
        this.commissionRepository = commissionRepository;
        this.colorsRepository = colorsRepository;
    }

    @Override
    public void run(String... args) {

       addWorkers();
       addCommissions();
       addBus();
    }

    public void addWorkers(){

        Worker requester = new Worker(72139, "Leonardo Heitor Poglia", "leonardo@weg.net", "123", "requester", "pt", "machining", false, false, false, false, false, 24,  addColor(1), null);
        Worker analyst = new Worker(7334, "Vytor Augusto Rosa", "vytor@weg.net", "123", "analyst", "pt", "ti", false, false, false, false, false, 24, addColor(2), null);
        Worker analystSupreme = new Worker(7335, "Analista Augusto Rosa", "analista@weg.net", "123", "analyst", "pt", "ti", false, false, false, false, false, 24, addColor(3), null);
        Worker ti = new Worker(72123, "Ester Girelli", "ester@weg.net", "123", "ti", "pt", "rh", false, true, false, false, false, 24, addColor(4), null);
        Worker business = new Worker(72121, "Eduarda Campos", "eduarda@weg.net", "123", "business", "pt", "ti", false, false, false, false, false, 24, addColor(5), null);
        workerRepository.save(requester);
        workerRepository.save(analystSupreme);
        workerRepository.save(analyst);
        workerRepository.save(ti);
        workerRepository.save(business);
    }

    public Colors addColor(int code){
        Colors color = new Colors(code, "#00579D", "#1976d2", "#0090c5", "#448dca", "#64C3D5", "#C4C4C4", "#36802d", "#d33649", "#f6921d", "#FFFFFF");
        colorsRepository.save(color);

        return color;
    }

    public void addCommissions() {
        Commission commission1 = new Commission(1, "CPVM – Comissão de Processos de Vendas e Desenvolvimento de produtos", "CPVM");
        Commission commission2 = new Commission(2, "CPGCI – Comissão de Processos da Cadeia Integrada", "CPGCI");
        Commission commission3 = new Commission(3, "CPGPR – Comissão de Processos de Gestão de Projetos", "CPGPR");
        Commission commission4 = new Commission(4, "CGPN – Comitê de Gestão de Processos de Negócio", "CGPN");
        Commission commission5 = new Commission(5, "CTI – Comitê de TI", "CTI");
        Commission commission6 = new Commission(6, "CWBS – Comitê WEG Business Services", "CWBS");
        Commission commission7 = new Commission(7, "DTI – Diretoria de TI", "DTI");

        commissionRepository.save(commission1);
        commissionRepository.save(commission2);
        commissionRepository.save(commission3);
        commissionRepository.save(commission4);
        commissionRepository.save(commission5);
        commissionRepository.save(commission6);
        commissionRepository.save(commission7);
    }

    public void addBus(){
        Bu bu1 = new Bu(1, "WMO-I – WEG Motores Industrial");
        Bu bu2 = new Bu(2, "WMO-C – WEG Motores Comercial");
        Bu bu3 = new Bu(3, "WEN – WEG Energia");
        Bu bu4 = new Bu(4, "WAU – WEG Automação");
        Bu bu5 = new Bu(5, "WDS – WEG Digital e Sistemas");
        Bu bu6 = new Bu(6, "WDC – WEG Drives e Controls");
        Bu bu7 = new Bu(7, "WTI – WEG Tintas");
        Bu bu8 = new Bu(8, "WTD – WEG Transmissão e Distribuição");
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