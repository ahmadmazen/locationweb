package com.mazen.location.controllers;

import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mazen.location.entities.Location;
import com.mazen.location.repos.LocationRepository;
import com.mazen.location.service.LocationService;
import com.mazen.location.util.EmailUtil;
import com.mazen.location.util.ReportUtil;

@Controller
public class LocationController {

	@Autowired
	LocationService service;

	@Autowired
	EmailUtil emailUtil;

	@Autowired
	LocationRepository repository;

	@Autowired
	ReportUtil reportUtil;

	@Autowired
	ServletContext sc;

	@RequestMapping("/showCreate")
	public String showCreate() {
		return "createLocation";

	}

	@RequestMapping("/saveLoc")
	public String saveLocation(@ModelAttribute("location") Location location, ModelMap modelMap) {
		Location locationSaved = service.saveLocation(location);
		String msg = "Location Saved with id : " + locationSaved.getId();
		modelMap.addAttribute("msg", msg);

		emailUtil.sendEmail("ahmadmazen07@gmail.com", "Location Saved!", "Location Saved Successfully with the id :"
				+ locationSaved.getId() + " || name : " + locationSaved.getName());

		return "createLocation";
	}

	@RequestMapping("/displayLocations")
	public String displayLocations(ModelMap modelMap) {
		List<Location> locations = service.getAllLocations();

		modelMap.addAttribute("locations", locations);
		return "displayLocations";
	}

	@RequestMapping("deleteLocations")
	public String deleteLocation(@RequestParam("id") int id, ModelMap modelMap) {
		// Location location = service.getLocationById(id); // to reduce DB call
		Location location = new Location();
		location.setId(id);
		service.deleteLocation(location);

		List<Location> locations = service.getAllLocations();

		modelMap.addAttribute("locations", locations);
		return "displayLocations";

	}

	@RequestMapping("/showUpdate")
	public String showUpdate(@RequestParam("id") int id, ModelMap modelMap) {
		Location location = service.getLocationById(id);
		modelMap.addAttribute("location", location);

		return "updateLocation";

	}

	@RequestMapping("updateLoc")

	public String updateLocation(@ModelAttribute("location") Location location, ModelMap modelMap) {

		service.updateLocation(location);

		List<Location> locations = service.getAllLocations();

		modelMap.addAttribute("locations", locations);
		return "displayLocations";
	}

	@RequestMapping("/generateReport")
	public String generateReport() {
		String path = sc.getRealPath("/");

		List<Object[]> data = repository.findTypeAndTypeCount();
		reportUtil.generatePieChart(path, data);
		return "report";

	}
}
