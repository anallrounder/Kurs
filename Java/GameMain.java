/*
201211 5번문제
두 사람이 게임을 진행한다. 이들의 이름을 키보드로 입력 받으며 각 사람은 Person 클래스로 작성하라. 
그러므로 프로그램에는 2개의 Person 객체가 생성되어야 한다. 두 사람은 번갈아 가면서 게임을 진행하는데 
각 사람이 자기 차례에서 <Enter> 키를 입력하면, 3개의 난수가 발생되고 이 숫자가 모두 같으면 승자가 되고 게임이 끝난다. 
난수의 범위를 너무 크게 잡으면 3개의 숫자가 일치하게 나올 가능성이 적기 때문에 숫자의 범위는 1~3까지로 한다.

1번째 선수 이름>>수희
2번째 선수 이름>>연수
[수희]:
3  1  1  아쉽군요!
[연수]:
3  1  3  아쉽군요!
[수희]:
2  2  1  아쉽군요!
[연수]:
1  1  2  아쉽군요!
[수희]:
3  3  3  수희님이 이겼습니다!
*/

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
