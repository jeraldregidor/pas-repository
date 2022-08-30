package com.example.demo.service;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.example.demo.model.Policy;
import com.example.demo.model.Vehicle;


@Component
public class RatingEngine {
    private double totalPremium = 0.0;

    public void settlePremium(Policy policy){
        totalPremium = 0;
        int dlx = LocalDate.now().getYear() - LocalDate.parse(policy.getPolicyHolder().getDrivLicIssueDate()).getYear();
        if(dlx == 0 ){
            dlx = 1;
        }
        double dlxDouble = Double.valueOf(dlx);

        for (Vehicle vehicle : policy.getVehicle()) {
            double vpf = getVpf(Integer.parseInt(vehicle.getYear()));
            
            /*
             *Formula for each premium per vehicle
                P (premium) = (vp x vpf) + ((vp/100)/dlx)
                    P = calculated premium
                    vp = vehicle purchase price
                    vpf = vehicle price factor
                    dlx = num of years since driver license was first issued
             */
            double computedPremium = (vehicle.getPurchasePrice() * vpf) + ((vehicle.getPurchasePrice()/100) / dlxDouble);
            vehicle.setEachPremium(computedPremium);
            System.out.println(vehicle.getEachPremium() + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            totalPremium += computedPremium;
        }
        policy.setPremium(totalPremium);

    }

    private double getVpf(int vehicleYear){
        int vehicleAge = (LocalDate.now().getYear() - vehicleYear);
        if(vehicleAge <= 1){
            return 1/10;
        }
        else if(vehicleAge <= 3){
            return 0.8/10;
        }
        else if(vehicleAge <= 5){
            return 0.7/10;
        }
        else if(vehicleAge <= 10){
            return 0.6/10;
        }
        else if(vehicleAge <= 15){
            return 0.4/10;
        }
        else if(vehicleAge <= 20){
            return 0.2/10;
        }
        else if(vehicleAge <= 40){
            return 0.1/10;
        }
        else{
            return 0.1/10;
        }
    }

}
