/*
201218_020 

## 8. 아래를 프로그래밍 하시오.

- Rectangle 배열 4개를 만든 후 스캐너 객체로 가로와 세로를 입력하여 4개의 객체를 배열에 할당한다
- getSortingRec 사각형 배열을 **내림 차순 정렬**한다.
- 정렬이 제대로 되었는지 배열에 저장된 객체의 getArea()함수를 순서대로 호출한다.

Rectangle[] rec = new Rectangle[4];
........
Rectangle[] recSorting = Rectangle.getSortingRec(rec)  
......
*/

package java_sort;
//Rectangle.getSortingRec(rec) //다시 공부할것 !! 필수!!!!!!!!

import java.util.Arrays;

class Rectangle implements Comparable {
	private int width;
	private int height;

	public Rectangle(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getArea() {
		return width * height;
	}

	public static Rectangle[] getSortingRec(Rectangle[] recs) {
		// 의도는 알고리즘을 써서 직접 소팅을 시켜봐라. -> 버블 소팅
//		Rectangle temp = null;
//
//		for (int i = 0; i < recs.length; i++) {
//			for (int j = 0; j < recs.length - i - 1; j++) { //버블소팅 복습 하기 : 응용할수있어야한다. 
//
//				if (recs[j].getArea() > recs[j + 1].getArea()) {
//					temp = recs[j];
//					recs[j] = recs[j + 1];
//					recs[j + 1] = temp;
//				}
//			}
//		}
		return recs;
		// 이렇게 하면 내림차순 오름차순 따로 정렬해야한다. 함수를 따로 만들던지 해서

//		Arrays.sort(recs);
	}

	// 음수의 의미 : 자리 바꿀 필요가 없음!!
	// 양수의 의미 : 자리 바꿈이 일어남 !!! 배열에서 앞에랑 뒤에 비교할 때 큰수느가 뒤로가야 오름차순
	@Override
	public int compareTo(Object o) {
//		return this.getArea() - ((Rectangle)o).getArea(); //두개 비교해서 양수다. 얘는 오름차순으로 가려면 자리 바꿔야함 
		return ((Rectangle) o).getArea() - this.getArea();
		// 5 - 3 양수 디폴트는 오름차순
	}
	// 내림차순으로 하려면 컴페어 투에서 순서를 바꾸면 된다.

}

class RecArrays {
	public static Rectangle[] sort(Object[] arrRecs) {
		Rectangle[] recs = (Rectangle[]) arrRecs;
		Rectangle temp = null;

		if (recs instanceof Comparable[]) { // 컴페어러블 구현 할 때

			for (int i = 0; i < recs.length; i++) {
				for (int j = 0; j < recs.length - i - 1; j++) {

					if (recs[j].compareTo(recs[j + 1]) > 0) { // 이걸 반드시 써먹어야한다.
						temp = recs[j];
						recs[j] = recs[j + 1];
						recs[j + 1] = temp;
						// 양수이면 자리바꿔서 오름차순으로 하겠다.
					}
				}
			}

		} else { // 컴페어러블 구현안할때
			for (int i = 0; i < recs.length; i++) {
				for (int j = 0; j < recs.length - i - 1; j++) {
					if (recs[j].getArea() > recs[j + 1].getArea()) {
						temp = recs[j];
						recs[j] = recs[j + 1];
						recs[j + 1] = temp;
					}
				}
			}

		}
		return recs;
	}
}

public class RecSortMain {

	public static void main(String[] args) {

		Rectangle[] recArr = { new Rectangle(6, 6), new Rectangle(5, 5), new Rectangle(10, 10), new Rectangle(12, 12),
				new Rectangle(11, 11) };

//		Rectangle.getSortingRec(recArr); //우리가 정렬한 오름차순 정렬 됨 
		RecArrays.sort(recArr);

		for (Rectangle rec : recArr) {
			System.out.println(rec.getArea());
		}

	}
}


//중요한점//음수와 양수의 의미//array.sort를 만들때 개발자에게강제 시킴 컴페어러블 상속받아 저컴페어 투만 정의해라. 그럼 위에꺼 호출해주겠다.
