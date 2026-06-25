package com.finsight.finsight.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "budget")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double monthlyIncome;

    private Double monthlyExpenses;

    private Double currentSavings;

    private Double emergencyFund;

    private Double monthlyEmi;

    private Double upcomingBills;

    private String financialGoal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(Double monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public Double getMonthlyExpenses() {
        return monthlyExpenses;
    }

    public void setMonthlyExpenses(Double monthlyExpenses) {
        this.monthlyExpenses = monthlyExpenses;
    }

    public Double getCurrentSavings() {
        return currentSavings;
    }

    public void setCurrentSavings(Double currentSavings) {
        this.currentSavings = currentSavings;
    }

    public Double getEmergencyFund() {
        return emergencyFund;
    }

    public void setEmergencyFund(Double emergencyFund) {
        this.emergencyFund = emergencyFund;
    }

    public Double getMonthlyEmi() {
        return monthlyEmi;
    }

    public void setMonthlyEmi(Double monthlyEmi) {
        this.monthlyEmi = monthlyEmi;
    }

    public Double getUpcomingBills() {
        return upcomingBills;
    }

    public void setUpcomingBills(Double upcomingBills) {
        this.upcomingBills = upcomingBills;
    }

    public String getFinancialGoal() {
        return financialGoal;
    }

    public void setFinancialGoal(String financialGoal) {
        this.financialGoal = financialGoal;
    }
}