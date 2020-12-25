import java.util.Scanner;

/*class Person {
	private int num1, num2, num3;
	public String name;

	public Person(String name) {
		this.name = name;
	}

	public boolean game() {
		num1 = (int) ((Math.random() * 3) + 1);
		num2 = (int) ((Math.random() * 3) + 1);
		num3 = (int) ((Math.random() * 3) + 1);

		System.out.print("\t" + num1 + "  " + num2 + "  " + num3 + "  ");

		if (num1 == num2 && num2 == num3)
			return true;
		else
			return false;
	}
}
*/
class Person {

	final int CHANCE = 4;
	private int[] numArr;
	public String name;

	public Person(String name) {
		this.name = name;
		numArr = new int[CHANCE];
	}

	public boolean game() {

		boolean isDuplicate = true;

		for (int i = 0; i < numArr.length; i++) {
			numArr[i] = (int) ((Math.random() * 3) + 1);
		}

		for (int i = 0; i <numArr.length ; i++) {
			if (numArr[0] != numArr[i] ) {
				isDuplicate= false;
				break;
			}

		}


		for(int i = 0; i < numArr.length; i++) {
			System.out.print("\t" + numArr[i] +  "  ");
		}
		return isDuplicate;

	}
}

public class GameMain {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		System.out.print("1번째 선수 이름>>");
		String name = sc.next();
		Person person1 = new Person(name);

		System.out.print("2번째 선수 이름>>");
		name = sc.next();
		Person person2 = new Person(name);

		String buffer = sc.nextLine();

		while (true) {
			System.out.print("[" + person1.name + "]:<Enter>");
			buffer = sc.nextLine();

			if (person1.game()) {
				System.out.println(person1.name + "님이 이겼습니다!");
				break;
			}
			System.out.println("아쉽군요!");
			System.out.print("[" + person2.name + "]:<Enter>");
			buffer = sc.nextLine();
			if (person2.game()) {
				System.out.println(person2.name + "님이 이겼습니다!");
				break;
			}
			System.out.println("아쉽군요!");
		}
		sc.close();

	}

}
