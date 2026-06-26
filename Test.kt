// 1. 최상위 변수 선언 (전역 변수)
val prefix = "[🤖 KOTLIN]"

fun main() {
    // 2. 변수 선언 (val: 읽기 전용 상수, var: 변경 가능 변수)
    val name = "Minwoo"
    
    // 3. 문자열 템플릿 사용 ($ 기호로 변수 출력)
    println("$prefix Hello, $name!")

    // 4. 클래스 인스턴스 생성 (new 키워드 없음)
    val calc = Calculator()
    val result = calc.add(15, 30)
    
    // 5. 수식 계산 템플릿 처리 (${} 사용)
    println("Result: ${result}")
}

// 6. 클래스 정의 및 단일 표현식 함수
class Calculator {
    // 중괄호와 return을 생략한 간결한 함수 표현
    fun add(a: Int, b: Int): Int = a + b
}