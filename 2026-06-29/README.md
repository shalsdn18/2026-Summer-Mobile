### 1. Summary

* **Date:** 2026-06-29
* **Topic:** Kotlin Data Structures and Object-Oriented Programming (OOP)
* **Scope:** Arrays, Loops, Collections (List, Set, Map), Class definitions, Inheritance, Data Classes, and Companion Objects.
* **Conclusion:** Consolidated reference for Kotlin collection manipulation, memory-efficient array declarations, and class hierarchy design mechanics.

### 2. Structured Table

| 분류 (Category) | 구성 요소 (Component) | 문법 및 특징 (Syntax & Description) |
| --- | --- | --- |
| **Array** | `Array<T>`, `IntArray` | `Array(size) { init }`. 기본형은 `IntArray`로 박싱 오버헤드 방지. |
| **Loop** | `for` | `in data`, `in data.indices`, `in data.withIndex()` |
| **Collection** | `List` | 순서화됨, 중복 허용 (`listOf`) |
| **Collection** | `Set` | 순서 없음, 중복 불가 (`setOf`) |
| **Collection** | `Map` | Key-Value 구조. `Pair(k,v)` 또는 `k to v` 로 초기화 (`mapOf`) |
| **OOP** | Constructor | 주 생성자(헤더 선언, `init` 블록), 보조 생성자(`constructor`) |
| **OOP** | Inheritance | 슈퍼클래스/메서드에 `open` 명시, 서브클래스에서 `override` |
| **Class** | `data class` | 주 생성자 프로퍼티 기준으로 `equals()`, `hashCode()` 등 자동 생성 |
| **Class** | `companion object` | 정적(Static) 멤버. 인스턴스화 없이 `ClassName.member`로 접근 |

### 3. Step-by-step Explanation / Implementation

#### A. 배열 및 반복문 (Array & Loops)

* **배열 초기화 로직:** 크기와 람다식을 통한 초기화 진행.
* `val data1 = Array(3, {0})`
* `val data2 = Array(5, {i -> i - 2})` (람다식을 활용한 인덱스 기반 초기화)


* **반복문 제어(FSM):** 요구되는 출력 형태에 따라 순회 방식을 다르게 적용.
1. 값 순회: `for(item in intData)`
2. 인덱스 순회: `for(i in intData.indices)`
3. 인덱스 및 값 동시 순회: `for((i, v) in array.withIndex())`



#### B. 콜렉션 (Collections: List, Set, Map)

* **List & Set:** `withIndex()`를 통해 동일한 방식으로 인덱스와 값 추출 가능.
* **Map 데이터 처리:**
* 생성: `mapOf(1 to "Mon", Pair("3", "Wed"))`
* 접근: Map 객체는 세 가지 프로퍼티를 통해 순회 가능.
* `mapData.keys`: Key만 순회
* `mapData.values`: Value만 순회
* `mapData.entries`: Key-Value 쌍 순회 (`for((k, v) in mapData.entries)`)





#### C. 객체 지향 프로그래밍 (Classes & OOP)

* **생성자 (Constructors):**
* 주 생성자에 `val`/`var`를 선언하면 자동으로 클래스 멤버로 바인딩 됨.
* 주 생성자 로직은 `init { }` 블록에서 실행.
* 보조 생성자(`constructor`)를 사용할 경우, `this()`를 통해 반드시 주 생성자나 다른 보조 생성자를 호출(위임)해야 함.


* **상속 및 오버라이딩 (Inheritance):**
* Kotlin의 클래스는 기본적으로 `final`임. 상속을 허용하려면 부모 클래스에 `open` 키워드 추가.
* 재정의가 필요한 변수와 함수 역시 `open`으로 선언 후, 자식 클래스에서 `override` 키워드로 덮어씀.



#### D. 특수 목적 클래스 (Data & Companion Class)

* **Data Class:**
* 클래스 비교(`equals`) 시 **주 생성자(Primary Constructor)에 선언된 프로퍼티만 비교**함.
* 객체 생성 후 클래스 바디 내에서 초기화된 변수(예: `email`)는 `equals()` 비교 대상에서 배제됨.


* **Companion Object:**
* 클래스 내부에 정의된 객체로, Java의 `static`과 동일한 역할 수행.
* 메모리에 단일 적재되며, 인스턴스 생성 없이 `클래스명.변수`, `클래스명.함수()` 형태로 직접 호출하여 사용.