/*
수정 필요함_다시 연습할 문제 

201211_09. 프로그래밍 (자바의 정석: 연습 문제 56page)
[7-19] 다음은 물건을 구입하는 사람을 정의한 Buyer클래스이다. 이 클래스는 멤버변수 로 돈(money)과 장바구니(cart)를 가지고 있다. 
제품을 구입하는 기능의 buy메서드와 장 바구니에 구입한 물건을 추가하는 add메서드, 구입한 물건의 목록과 사용금액, 
그리고 남 은 금액을 출력하는 summary메서드를 완성하시오.

1. 메서드명 : buy
	 기 능 : 지정된 물건을 구입한다. 가진 돈(money)에서 물건의 가격을 빼고, 
					장바구니(cart)에 담는다.
					만일 가진 돈이 물건의 가격보다 적다면 바로 종료한다.
	 반환타입 : 없음
	 매개변수 : Product p - 구입할 물건

2. 메서드명 : add
	 기 능 : 지정된 물건을 장바구니에 담는다. 만일 장바구니에 담을 공간이 없으면, 
					장바구니의 크기를 2배로 늘린 다음에 담는다.
	 반환타입 : 없음
	 매개변수 : Product p - 구입할 물건

3. 메서드명 : summary
	 기 능 : 구입한 물건의 목록과 사용금액, 남은 금액을 출력한다.
	 반환타입 : 없음
	 매개변수 : 없음
*/

package java_1211;

public class polyBuy {
	public static void main(String args[]) {
		Buyer b = new Buyer();
		b.buy(new Tv());
		b.buy(new Computer());
		b.buy(new Tv());
		b.buy(new Audio());
		b.buy(new Computer());
		b.buy(new Computer());
		b.buy(new Computer());
		b.summary();
	} //end of main
} //end of polyBuy

class Buyer {
	int money = 1000;
	Product[] cart = new Product[3]; // 구입한 제품을 저장하기 위한 배열
	int i = 0; 						// Product배열 cart에 사용될 index
	
	void buy(Product p) {
		for(i = 0; i < cart.length; i++) {	
			if(money < p.getPrice()) { 
				return;
			} else {
				money = money - p.getPrice();	
				add(p);	
			}
		}
		/*
		 (1) 아래의 로직에 맞게 코드를 작성하시오.
		 1.1 가진 돈과 물건의 가격을 비교해서 가진 돈이 적으면 메서드를 종료한다.
		 1.2 가진 돈이 충분하면, 제품의 가격을 가진 돈에서 빼고
		 1.3 장바구니에 구입한 물건을 담는다.(add메서드 호출)
		 */
	} //end of buy

	
	void add(Product p) {
		if(i >= cart.length) {					
			Product[] tempCart = new Product[cart.length * 2];	
			System.arraycopy(cart, 0, tempCart, 0, cart.length);	
			cart = tempCart;					
			cart[i] = p;							
		}else {
			cart[i] = p;
		}
		i++;
		/*
		(2) 아래의 로직에 맞게 코드를 작성하시오.
		1.1 i의 값이 장바구니의 크기보다 같거나 크면
		1.1.1 기존의 장바구니보다 2배 큰 새로운 배열을 생성한다. 
		1.1.2 기존의 장바구니의 내용을 새로운 배열에 복사한다.
		1.1.3 새로운 장바구니와 기존의 장바구니를 바꾼다.
		1.2 물건을 장바구니(cart)에 저장한다. 그리고 i의 값을 1 증가시킨다.
		 */
	} //end of add
	
	void summary() {
		int sum=0;
		String list = null;
		
		System.out.println("장바구니 목록: ");
		for(Product e: cart) {
			list = list + cart[i] + "\n";
			System.out.println( e + list);
		}
		for(Product e: cart) {
			sum = sum + e.getPrice() ; 
		}
		System.out.println("장바구니 총 금액: "+ sum + "");
		
		System.out.println("잔액: "+ money + "원");
		/*
		(3) 아래의 로직에 맞게 코드를 작성하시오.
		1.1 장바구니에 담긴 물건들의 목록을 만들어 출력한다.
		1.2 장바구니에 담긴 물건들의 가격을 모두 더해서 출력한다.
		1.3 물건을 사고 남은 금액(money)를 출력한다.
		*/
	} //end of summary

} //end of Buyer

class Product {
	int price; // 제품의 가격
	String productName;
	
	Product(){
		
	}
	
	Product(int price) {
		this.price = price;
	}
	
	public String toString() { //이름 스트링으로 반환 
		return null; 
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	
}

class Tv extends Product {
	Tv() { 				//생성자 함수 
		super(100); 
	}

	public String toString() { //이름 스트링으로 반환 
		return "Tv"; 
	}
}

class Computer extends Product {
	Computer() { 
		super(200); 
	}

	public String toString() { 
		return "Computer";
	}
}

class Audio extends Product {
	Audio() { 
		super(50); 
	}

	public String toString() { 
		return "Audio";
	}
}
