package views;

import dao.UserDao;
import model.User;
import services.GenerateOTP;
import services.SendOTPService;
import services.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Scanner;

public class Welcome {
    public void welcomeScreen(){
        BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome To The APP");
        System.out.println("Press 1 to login");
        System.out.println("Press 2 to signup");
        System.out.println("Press 0 to exit");

        int choice=0;
        try {
            choice= Integer.parseInt(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        switch (choice){
            case 1 -> login();
            case 2 -> signup();
            case 0 -> System.exit(0);
        }
    }

    private void signup() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter name");
        String name = sc.nextLine();
        System.out.println("Enter email");
        String email = sc.nextLine();
        String genOTP = GenerateOTP.getOTP();
        SendOTPService.sendOTP(email, genOTP);
        System.out.println("Enter the otp");
        String otp = sc.nextLine();
        if(otp.equals(genOTP)) {
            User user = new User(name, email);
            int response = UserService.saveUser(user);
            switch (response) {
                case 1 -> System.out.println("User registered");
                case 0 -> System.out.println("User already exists");
            }
        } else {
            System.out.println("Wrong OTP");
        }
    }

    private void login() {
        Scanner sc= new Scanner(System.in);
        System.out.println("Enter Email");
        String email= sc.nextLine();
        try{
            if(UserDao.isExists(email)){
                String genOTP= GenerateOTP.getOTP();
                SendOTPService.sendOTP(email, genOTP);
                System.out.println("Enter the otp");
                String otp= sc.nextLine();
                if(otp.equals(genOTP)){
                    new UserView(email).name();

                }else{
                    System.out.println("Wrong OTP");
                }
            }else{
                System.out.println("User not found");
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }
}
