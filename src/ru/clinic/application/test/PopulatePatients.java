package ru.clinic.application.test;

import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.dao.PatientsDao;
import ru.clinic.application.java.dao.entity.Admin;
import ru.clinic.application.java.service.AdminService;

import java.util.Random;

/**
 * Created by Artem Siatchinov on 1/7/2017.
 */

@Component
public class PopulatePatients {
    
    private final String lastNames = "Иванов Смирнов Кузнецов Попов Васильев Петров Соколов Михайлов Новиков Федоров Морозов Волков Алексеев Лебедев Семенов Егоров " +
            "Павлов Козлов Степанов Николаев Орлов Андреев Макаров Никитин Захаров Зайцев Соловьев Борисов Яковлев Григорьев Романов Воробьев Сергеев Кузьмин Фролов Александров " +
            "Дмитриев Королев Гусев Киселев Ильин Максимов Поляков Сорокин Виноградов Ковалев Белов Медведев Антонов Тарасов Жуков Баранов " +
            "Филиппов Комаров Давыдов Беляев Герасимов Богданов Осипов Сидоров Матвеев Титов Марков Миронов Крылов Куликов Карпов Власов Мельников";

    private final String firstNames = "Кирилл Никита Антон Степан Константин Савва Евгений Яков Ефрем Василий Константин Алексей Макар Денис Виктор Тимофей Михаил Ростислав " +
            "Юрий Максим Никон Андрей Никита Захар Денис Арсений Артем Валентин Виктор Иван Елисей Гавриил Петр Дмитрий Валерий Макар Денис Михаил Роман Владимир Станислав Федор " +
            "Макар Глеб Евдоким Николай Константин Максим Семен Степан Антон Виктор Федор Фома Илья Роман Андрей Дмитрий Константин Игнат Максим Кузьма " +
            "Евгений Марк Андрей Матвей Юлиан Макар Александр Василий Григорий Всеволод Ярослав";

    private final String middleNames = "Александрович Алексеевич Анатольевич Андреевич Антонович Аркадьевич Артемович Богданович Борисович Валентинович Валерьевич Васильевич " +
            "Викторович Витальевич Владимирович Владиславович Вячеславович Геннадиевич Георгиевич Григорьевич Данилович Денисович Дмитриевич Евгеньевич Егорович Ефимович Иванович " +
            "Игоревич Ильич Иосифович Кириллович Константинович Леонидович Львович Максимович Матвеевич Михайлович Николаевич Олегович Павлович Петрович Платонович Робертович Романович " +
            "Семенович Сергеевич Станиславович Степанович Тарасович Тимофеевич Федорович Феликсович Филиппович Эдуардович Юрьевич Яковлевич Ярославович";

    @Autowired
    PatientsDao patientsDao;

    @Autowired
    AdminService adminService;

    public PopulatePatients(){}

    public void populateRandomPatients(int count){
        //public void addNewPatient(int creatorId, String lastName, String firstName, String middleName, String phoneNumber, String phoneNumberTwo, String email, String comment)

        String[] last = lastNames.split(" ");
        String[] first = firstNames.split(" ");
        String[] middle = middleNames.split(" ");

        ObservableList<Admin> admins = adminService.getAdmins();


        Random random =  new Random();
        for (int index = 0; index < count ; index++){

            int lastInt = random.nextInt(last.length);
            int middleInt = random.nextInt(middle.length);
            int firstInt = random.nextInt(first.length);
            int adminInt = random.nextInt(admins.size());
            String phoneNumber = String.valueOf(random.nextInt(999)) + random.nextInt(8000000);
            String phoneNumberTwo = String.valueOf(random.nextInt(999)) + random.nextInt(8000000);

            String email = last[lastInt] + "@gmail.com";

            String comment = phoneNumber + "  " + phoneNumberTwo + email;


            if (index%3 == 0) {
                patientsDao.addNewPatient(admins.get(adminInt).getId(), null, first[firstInt], middle[middleInt], phoneNumber, null, email, comment);
            }
            else if (index%7 == 0) {
                patientsDao.addNewPatient(admins.get(adminInt).getId(), last[lastInt], first[firstInt], middle[middleInt], null, phoneNumberTwo, email, null);
            }
            else if (index%9 == 0) {
                patientsDao.addNewPatient(admins.get(adminInt).getId(), last[lastInt], first[firstInt], null, phoneNumber, phoneNumberTwo, null, comment);
            }
            else if (index%13 == 0) {
                patientsDao.addNewPatient(admins.get(adminInt).getId(), last[lastInt], first[firstInt], null, null, null, email, comment);
            }else {
                patientsDao.addNewPatient(admins.get(adminInt).getId(), last[lastInt], first[firstInt], middle[middleInt], phoneNumber, phoneNumberTwo, email, comment);
            }
            System.out.println("Count = " + index);

        }
    }
}
