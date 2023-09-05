import java.util.Scanner;

public class AuthenticationExample {
	public static void main(String[] args) {
		UserAuthentication authentication = DrupalJSONAuth.getInstance();
		
		Scanner input = new Scanner(System.in);
		
		System.out.print("Enter the NUN display name: ");
		
		String username = input.next();
		
		System.out.print("Enter the NUN password: ");
		
		String password = input.next();
		
		input.close();
		
		System.out.println(authentication.authenticate(username, password));
	}
}
