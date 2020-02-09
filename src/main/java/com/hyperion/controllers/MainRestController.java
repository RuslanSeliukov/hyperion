package com.hyperion.controllers;

import com.hyperion.data.dao.EmployeeDao;
import com.hyperion.data.dao.ExtraWorkDao;
import com.hyperion.data.entity.Employee;
import com.hyperion.data.entity.ExtraWork;
import com.hyperion.data.entity.FileEntity;
import com.hyperion.pojo.FormData;
import com.hyperion.pojo.SelectOption;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RequestMapping(path = "/api")
@RestController
public class MainRestController {

    @Autowired
    EmployeeDao employeeDao;
    @Autowired
    ExtraWorkDao extraWorkDao;

    @RequestMapping(method = GET, path = "/getWorkClass", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SelectOption> getWorkClass() {
        return new ArrayList<>(Arrays.asList(new SelectOption("social", "Общественные"),
                new SelectOption("administrat", "Административный"),
                new SelectOption("eco", "Экологические")));
    }

    @RequestMapping(method = GET, path = "/getEmployees", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SelectOption> getEmployees() {
        employeeDao.findAll();
        int i = 0;
        List<SelectOption> response = new ArrayList<>();
        for (Employee employee : employeeDao.findAll()) {
            response.add(new SelectOption(Integer.toString(i), employee.getName()));
            i++;
        }
        return response;
    }

    @RequestMapping(method = POST, path = "/submitForm")
    public String submitForm(@RequestBody FormData formData) {
        ExtraWork ew = new ExtraWork();
        ew.setWorkClass(formData.getWorkClass());
        ew.setWorkName(formData.getWorkName());
        ew.setDescription(formData.getDescription());
        ew.setDateStart(formData.getDateStart());
        ew.setDateEnd(formData.getDateEnd());
        ew.setAmountOfTime(formData.getAmountOfTime());

        List<String> listString = cast(formData.getEmployees());
        List<String> queryEmployees = new ArrayList<>();
        List<Employee> entityEmployees = new ArrayList<>();


        for (int i = 0; i < listString.size(); i++) {
            try {
                queryEmployees.add(listString.get(i));
            } catch (Exception e) {
                continue;
            }
        }

        for (String employee : queryEmployees) {
            entityEmployees.add(employeeDao.getEmployeeByName(employee));


            Employee em = employeeDao.getEmployeeByName(employee);
            List<ExtraWork> employeeExtraWork = employeeDao.getEmployeeByName(employee).getEmployeeExtraWork();

            employeeExtraWork.add(ew);
            em.setEmployeeExtraWork(employeeExtraWork);
            employeeDao.updateEmployee(em);

        }

        ew.setEmployees(entityEmployees);
        extraWorkDao.saveExtraWork(ew);

        return "success";
    }


    @RequestMapping(method = POST, path = "/submitEmployeeReport")
    public ResponseEntity<byte[]> submitEmployeeReport(@RequestBody FormData formData) {

        Employee employee = employeeDao.getEmployeeByName(formData.getEmployee());

        String fileHeaders = String.format("%-30s %-30s %-30s %-30s %-30s %-30s %-30s %-30s %n",
                "Дата начала", "Дата окончания", "Класс работы", "Название", "Описание", "Имя", "Фамилия", "Отдел");

        String[] fl = employee.getName().split(" ");
        String firstName = fl[0];
        String lastName = fl[1];

        List<ExtraWork> emExtraWork = employee.getEmployeeExtraWork();

        String mainDoc = "";

        SimpleDateFormat si = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (emExtraWork != null && emExtraWork.size() >= 1) {
                for (ExtraWork extraWork: emExtraWork) {
                    if (isBetweenDates(si.parse(formData.getDateStart()), si.parse(formData.getDateEnd()), si.parse(extraWork.getDateStart())) &&
                            isBetweenDates(si.parse(formData.getDateStart()), si.parse(formData.getDateEnd()), si.parse(extraWork.getDateEnd()))) {
                        mainDoc += String.format("%-30s %-30s %-30s %-30s %-30s %-30s %-30s %-30s %n",
                                extraWork.getDateStart(), extraWork.getDateEnd(), extraWork.getWorkClass(),
                                extraWork.getWorkName(), extraWork.getDescription(),
                                firstName, lastName, employee.getDepartment());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            File file = new File("236324637.txt");

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(fileHeaders);
            writer.write(mainDoc);

            writer.close();

            InputStream resource = new FileInputStream(file);

            FileEntity fileEntity = new FileEntity("report.txt", "application/octet-stream" , IOUtils.toByteArray(resource));

            HttpHeaders header = new HttpHeaders();

            header.setContentType(MediaType.valueOf(fileEntity.getContentType()));
            header.setContentLength(fileEntity.getData().length);
            header.set("Content-Disposition", "attachment; filename=" + fileEntity.getFileName());

            return new ResponseEntity<>(fileEntity.getData(), header, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(method = POST, path = "/submitClassWorkReport")
    public ResponseEntity<byte[]> submitClassWorkReport(@RequestBody FormData formData) {

        List<ExtraWork> extraWorks = extraWorkDao.getExtraWorkByName(formData.getWorkClass());

        String fileHeaders = String.format("%-30s %-30s %-30s %-30s %-30s %-30s %-30s %-30s %n",
                "Дата начала", "Дата окончания", "Класс работы", "Название", "Описание", "Имя", "Фамилия", "Отдел");

        String mainDoc = "";

        SimpleDateFormat si = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (extraWorks != null && extraWorks.size() >= 1) {
                for (ExtraWork extraWork: extraWorks) {
                    if (isBetweenDates(si.parse(formData.getDateStart()), si.parse(formData.getDateEnd()), si.parse(extraWork.getDateStart())) &&
                            isBetweenDates(si.parse(formData.getDateStart()), si.parse(formData.getDateEnd()), si.parse(extraWork.getDateEnd()))) {
                        for (Employee employee: extraWork.getEmployees()) {
                            mainDoc += String.format("%-30s %-30s %-30s %-30s %-30s",
                                    extraWork.getDateStart(), extraWork.getDateEnd(), extraWork.getWorkClass(),
                                    extraWork.getWorkName(), extraWork.getDescription());
                            String[] arr = employee.getName().split(" ");
                            mainDoc += String.format("%-30s %-30s %-30s %n",
                                    arr[0], arr[1], employee.getDepartment());

                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            File file = new File("236324637.txt");

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(fileHeaders);
            writer.write(mainDoc);

            writer.close();

            InputStream resource = new FileInputStream(file);

            FileEntity fileEntity = new FileEntity("report.txt", "application/octet-stream" , IOUtils.toByteArray(resource));

            HttpHeaders header = new HttpHeaders();

            header.setContentType(MediaType.valueOf(fileEntity.getContentType()));
            header.setContentLength(fileEntity.getData().length);
            header.set("Content-Disposition", "attachment; filename=" + fileEntity.getFileName());

            return new ResponseEntity<>(fileEntity.getData(), header, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isBetweenDates (Date a, Date b, Date d) {
        return a.compareTo(d) * d.compareTo(b) >= 0;
    }

    @SuppressWarnings("unchecked")
    public static <T extends List<?>> T cast(Object obj) {
        return (T) obj;
    }
}
