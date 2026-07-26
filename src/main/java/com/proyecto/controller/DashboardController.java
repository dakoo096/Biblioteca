package com.proyecto.controller;

import com.proyecto.dto.response.DashboardDTO;
import com.proyecto.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping({ "/", "/dashboard" })
    public String verDashboard(Model model) {
        DashboardDTO dashboardData = dashboardService.obtenerMétricasDashboard();
        model.addAttribute("dashboard", dashboardData);
        return "dashboard";
    }
}
