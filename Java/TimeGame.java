/*
201218_020 5번문제

## 5. 경과 시간을 맞추는 게임을 작성하라.

다음 예시를 참고하면, <Enter> 키를 입력하면 현재 초 시간을 보여주고 
여기서 10초에 더 근접하도록 다음 <Enter> 키를 입력한 사람이 이기는 게임이다.

```
10초에 가까운 사람이 이기는 게임입니다.
황기태 시작 키  >>
현재 초 시간 = 42
10초 예상 후 키  >>
현재 초 시간 = 50
이재문 시작 키  >>
현재 초 시간 = 51
10초 예상 후 키  >>
현재 초 시간 = 4
황기태의 결과 8, 이재문의 결과 13, 승자는 황기태
```
*/

public class TimeGame {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		SimpleDateFormat format1 = new SimpleDateFormat("s");

		int time1;
		int time2;
		Gamer[] g1 = { new Gamer("황기태"), new Gamer("이재문") };

		System.out.println("10초에 가까운 사람이 이기는 게임입니다.");
		for (int i = 0; i < 2; i++) {
			System.out.print(g1[i] + " 시작 키 >>");
			sc.nextLine();
			time1 = Integer.valueOf(format1.format(System.currentTimeMillis()));
			System.out.println("현재 초 시간 = " + time1 + "\n");
			System.out.print("10초 예상 후 키 >>");
			sc.nextLine();
			time2 = Integer.valueOf(format1.format(System.currentTimeMillis()));
			System.out.println("현재 초 시간 = " + time2 + "\n");
			g1[i].time = time2 - time1;

		}
		if (10 - g1[0].time > 10 - g1[1].time) {
			System.out.printf("%s의 결과는 %d, %s의 결과는 %d, 승자는 %s", g1[0].name, g1[0].time, g1[1].name, g1[1].time,
					g1[1].name);
		} else
			System.out.printf("%s의 결과는 %d, %s의 결과는 %d, 승자는 %s", g1[0].name, g1[0].time, g1[1].name, g1[1].time,
					g1[0].name);
	}
}

class Gamer {
	public int time;
	public String name;

	public Gamer(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
