import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final int NUM_DOCTORS = 5;
    private static final boolean[][][] schedules; // Matrix to represent each doctor's schedule
    private static final String[] doctorNames = {"Xing", "Sal", "Ying", "Ra", "Vakhim"};
    private static final String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    private record Appointment(String doctorName, String clientName, String clientTelephone, String day, int timeSlot) {
        static Scanner input = new Scanner(System.in);
        @Override
            public String toString() {
                return "Client's name: " + clientName + ", Telephone: " + clientTelephone+" has an appointment wiht Dr."+doctorName+" on "+day+" , " + (timeSlot + 1)+" O'clock";
            }
        }

    private static final List<Appointment> bookedAppointments = new ArrayList<>();

    // Function to search for a client in the booking history (case-insensitive)
    private static void searchClientBookingHistory() {
        System.out.println("\n");
        System.out.print("Enter client name to search in booking history: ");
        Scanner scanner = new Scanner(System.in);
        String clientName = scanner.nextLine().toLowerCase(); // Convert to lowercase for case-insensitive comparison

        boolean found = false;

        for (Appointment appointment : bookedAppointments) {
            if (appointment.clientName.equalsIgnoreCase(clientName)) {
                System.out.println("----------------------------");
                System.out.println(appointment);
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("-----------------------------------------");
            System.out.println("No booking found for client name " + clientName);
            System.out.println("-----------------------------------------");
            System.out.println("\n----- press Enter to continue -----\n");
            scanner.nextLine();
        }
    }

    static {
        schedules = new boolean[NUM_DOCTORS][7][8];
    }

    // Function to check if a specific doctor is available on a specific day and time
    private static boolean isDoctorAvailable(int doctorIndex, int day, int timeSlot) {
        if (day >= 0 && day < schedules[doctorIndex].length) {
            return !schedules[doctorIndex][day][timeSlot];
        }
        return false;
    }

    // Function to book an appointment for a specific doctor
    private static void bookAppointment(int doctorIndex, int day, int timeSlot, String clientName, String clientTelephone) {
        Scanner scanner = new Scanner(System.in);
        schedules[doctorIndex][day][timeSlot] = true;

        Appointment appointment = new Appointment(
                doctorNames[doctorIndex],
                clientName,
                clientTelephone,
                daysOfWeek[day],
                timeSlot
        );

        bookedAppointments.add(appointment);
        System.out.println("---------------------------------------------------");
        System.out.println("\nAppointment booked successfully with Dr. " + doctorNames[doctorIndex] + "!");
        System.out.println("Client Name: " + clientName +" Telephone: " + clientTelephone);
        System.out.println("---------------------------------------------------");
        System.out.println("\n----- press Enter to continue -----\n");
        scanner.nextLine();

    }

    // Function to display the availability for a specific doctor and provide the option to book
    private static void displayAvailabilityWithBooking(int doctorIndex, Scanner scanner) {
        System.out.println("--------------------------------------------------------------------");
        System.out.println("\nDr. " + doctorNames[doctorIndex] + "'s Availability:\n");

        System.out.println("  Days    |   1       2      3      4      5      6      7      8    ");
        System.out.println("+---------+-------+------+------+------+------+------+------+------+");
        for (int day = 0; day < daysOfWeek.length; day++) {
            System.out.printf("%-9s | ", daysOfWeek[day]);
            for (int timeSlot = 0; timeSlot < schedules[doctorIndex][day].length; timeSlot++) {
                System.out.print(isDoctorAvailable(doctorIndex, day, timeSlot) ? "______ " : "Booked ");
            }
            System.out.println("");
        }

        System.out.print("\nDo you want to book an appointment now? (yes/no): ");
        String response = scanner.next();

        if (response.equalsIgnoreCase("yes")) {
            System.out.print("\nEnter day (Monday-Sunday): ");
            String day = scanner.next().toLowerCase();

            int dayIndex = -1;
            for (int i = 0; i < daysOfWeek.length; i++) {
                if (daysOfWeek[i].toLowerCase().startsWith(day)) {
                    dayIndex = i;
                    break;
                }
            }

            if (dayIndex != -1) {
                System.out.print("Enter time slot (1-8): ");
                int timeSlot = scanner.nextInt() - 1;

                if (timeSlot >= 0 && timeSlot < 8) {
                    scanner.nextLine(); // Consume the newline character
                    System.out.print("Enter your name: ");
                    String clientName = scanner.nextLine();

                    System.out.print("Enter your telephone: ");
                    String clientTelephone = scanner.nextLine();

                    if (isDoctorAvailable(doctorIndex, dayIndex, timeSlot)) {
                        bookAppointment(doctorIndex, dayIndex, timeSlot, clientName, clientTelephone);
                    } else {
                        System.out.println("\nSorry, Dr. " + doctorNames[doctorIndex] +
                                " is not available at that time.");
                        System.out.println("\n----- press Enter to continue -----\n");
                        scanner.nextLine();
                    }
                } else {
                    System.out.println("\nInvalid time slot. Booking canceled.");
                }
            } else {
                System.out.println("\nInvalid day. Booking canceled.");
            }
        }
    }

    // Function to display the list of doctors with names
    private static void displayDoctorList() {
        System.out.println("\nList of Doctors:\n");

        for (int i = 0; i < NUM_DOCTORS; i++) {
            System.out.println((i + 1) + ". Dr. " + doctorNames[i]);
        }
        System.out.println("");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("");
            System.out.println("--------------------------------------");
            System.out.println("1. Book Appointment");
            System.out.println("2. Display Doctor's Availability");
            System.out.println("3. View Booking History");
            System.out.println("4. Exit");
            System.out.println("--------------------------------------");
            System.out.print("\nSelect An option above for : ");
        try{
            int choice = scanner.nextInt();

            switch (choice) {
                case 1, 2 -> {
                    displayDoctorList();
                    try{
                        System.out.print("Select a doctor by entering the corresponding number: ");
                        int selectedDoctor = scanner.nextInt() - 1;
                        if (selectedDoctor >= 0 && selectedDoctor < NUM_DOCTORS) {
                            displayAvailabilityWithBooking(selectedDoctor, scanner);
                        } else {
                            System.out.println("Invalid doctor selection. Please try again.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("-------------------------------------------");
                        System.out.println("Invalid input. Please enter a valid number.");
                        System.out.println("-------------------------------------------");
                        System.out.println("\n----- press Enter to continue -----\n");
                        scanner.nextLine();
                        scanner.nextLine();
                    }
                }
                case 3 -> searchClientBookingHistory();
                case 4 -> {
                    System.out.println("Exiting the program. Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } catch (InputMismatchException e) {
            System.out.println("-------------------------------------------");
            System.out.println("Invalid input. Please enter a valid number.");
            System.out.println("-------------------------------------------");
            System.out.println("\n----- press Enter to continue -----\n");
            scanner.nextLine();
            scanner.nextLine();
            }
        }
    }
}
