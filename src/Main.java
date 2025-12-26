import java.sql.*;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        while (true) {
            System.out.println("""
            \n=== BUS AGENCY SYSTEM ===
            1. Add Bus
            2. View Buses
            3. Book Seat
            4. Exit
            """);

            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> addBus();
                case 2 -> viewBuses();
                case 3 -> bookSeat();
                case 4 -> System.exit(0);
                default -> System.out.println("❌ Invalid choice");
            }
        }
    }

    // 🔹 Add Bus
    static void addBus() {
        try (Connection con = DB.getConnection()) {

            sc.nextLine();
            System.out.print("Bus Name: ");
            String name = sc.nextLine();
            System.out.print("Source: ");
            String src = sc.nextLine();
            System.out.print("Destination: ");
            String dest = sc.nextLine();
            System.out.print("Seats: ");
            int seats = sc.nextInt();

            String q = "INSERT INTO buses(bus_name,source,destination,seats) VALUES(?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(q);

            ps.setString(1, name);
            ps.setString(2, src);
            ps.setString(3, dest);
            ps.setInt(4, seats);

            ps.executeUpdate();
            System.out.println("✅ Bus added");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔹 View Buses
    static void viewBuses() {
        try (Connection con = DB.getConnection()) {

            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM buses");

            System.out.println("\nID | Bus | Route | Seats");
            while (rs.next()) {
                System.out.println(
                        rs.getInt("bus_id") + " | " +
                                rs.getString("bus_name") + " | " +
                                rs.getString("source") + " → " +
                                rs.getString("destination") + " | " +
                                rs.getInt("seats")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔹 Book Seat
    static void bookSeat() {
        try (Connection con = DB.getConnection()) {

            con.setAutoCommit(false);

            System.out.print("Bus ID: ");
            int id = sc.nextInt();

            String check = "SELECT seats FROM buses WHERE bus_id = ?";
            PreparedStatement checkPs = con.prepareStatement(check);
            checkPs.setInt(1, id);

            ResultSet rs = checkPs.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {

                String update = "UPDATE buses SET seats = seats - 1 WHERE bus_id = ?";
                PreparedStatement up = con.prepareStatement(update);
                up.setInt(1, id);
                up.executeUpdate();

                con.commit();
                System.out.println("🎟️ Seat booked");

            } else {
                con.rollback();
                System.out.println("❌ No seats available");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
