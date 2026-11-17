Лаб 10: Өгөгдлийн урсгалын тест — Definition-Use хосууд
F.CSA313 — Программ хангамжийн чанарын баталгаа ба тест (2026)
- Нэр: З.Жаргалмаа
- Оюутны код: B232270005
- GitHub: https://github.com/jargalmaaaaa19/lab10-dataflow

Орчин
user@DESKTOP-9HU6DH7:~/lab10-dataflow$ java -version
openjdk version "17.0.21.0.3" 2026-10-24
OpenJDK Runtime Environment (build 17.0.21.0.3+1-1-24.04.1-Ubuntu)
OpenJDK 64-Bit Server VM (build 17.0.21.0.3+1-1-24.04.1-Ubuntu, mixed mode, sharing)
user@DESKTOP-9HU6DH7:~/lab10-dataflow$ mvn -version
Apache Maven 3.8.7
Maven home: /usr/share/maven
Java version: 17.0.21.0.3, vendor: Ubuntu, runtime: /usr/lib/jvm/java-17-openjdk-amd64
Default locale: en, platform encoding: UTF-8
OS name: "linux", version: "6.6.87.2-microsoft-standard-wsl2", arch: "amd64", family: "unix"

Ажиллуулах:
git clone https://github.com/jargalmaaaaa19/lab10-dataflow && cd lab10-dataflow
mvn clean verify

Мутацийн туршилт бүрийн өмнө mvn clean test ашигласан. Учир нь maven-compiler-plugin 3.1 нь өөрчилсөн эх кодыг заримдаа "Nothing to compile" гэж алгасдаг тул мутаци compile хийгдэхгүй, туршилт хуурамч болдог. (Миний анхны M1–M3 гаралтад яг ингэж Nothing to compile, Failures: 0 гарсан тул clean-тэйгээр дахин ажиллуулж results/mvn-test-M*.txt-ийг шинэчилсэн.)

Файлын мөрийн дугаарлалт
Бүх мөрийн дугаар нь DataFlowTargets.java файлын мөр. Эх код starter-тай ижил (git diff 4120682 HEAD -- src/main хоосон).
Лекцийн дугаарлалтаас хөрвүүлэлт: 1→13, 3→15, 4→16, 5→17, 6→18, 8→20.

Репогийн бүтэц
src/main/java/mn/edu/must/sqat/DataFlowTargets.java   # өөрчлөөгүй
src/test/java/mn/edu/must/sqat/GcdTest.java           # 2 тест (Хэсэг A)
src/test/java/mn/edu/must/sqat/BonusTest.java         # 11 тест (Хэсэг B: 2 all-defs + 9 all-DU-pairs)
src/test/java/mn/edu/must/sqat/M2KillerTest.java      # 1 тест (M2-ийг унагана)
cfg-bonus.png, cfg-bonus.dot                          # bonus()-ийн CFG
results/                                              # mvn гаралт, jacoco csv

A: gcd
A1. Def / Use
Мөр | Код | Def | Use
13 | gcd(int x, int y) | x, y | -
14 | int tmp; | (зарлалт, утга оноохгүй) | -
15 | while (y != 0) | - | y (p-use)
16 | tmp = x % y; | tmp | x, y (c-use)
17 | x = y; | x | y (c-use)
18 | y = tmp; | y | tmp (c-use)
20 | return x; | - | x (c-use)

A2. DU хосын хүснэгт
Тестүүд (GcdTest.java): G1 = gcd_loopNotEntered → gcd(7,0); G2 = gcd_twoIterations → gcd(12,8).
№ | Хувьсагч | Def | Use | c/p-use | Def-clear зам | Тестийн ID | Хамрагдсан
1 | x | 13 | 16 | c | 13→15→16 | G2 | ✔
2 | x | 13 | 20 | c | 13→15→20 | G1 | ✔
3 | x | 17 | 16 | c | 17→18→15→16 | G2 (2-р давталт) | ✔
4 | x | 17 | 20 | c | 17→18→15→20 | G2 | ✔
5 | y | 13 | 15 | p (T/F) | 13→15 | T: G2, F: G1 | ✔
6 | y | 13 | 16 | c | 13→15→16 | G2 | ✔
7 | y | 13 | 17 | c | 13→15→16→17 (16 нь y-г өөрчилдөггүй) | G2 | ✔
8 | y | 18 | 15 | p (T/F) | 18→15 | T: G2 (давталт 1→2), F: G2 (төгсгөл) | ✔
9 | y | 18 | 16 | c | 18→15→16 | G2 (2-р давталт) | ✔
10 | y | 18 | 17 | c | 18→15→16→17 | G2 (2-р давталт) | ✔
11 | tmp | 16 | 18 | c | 16→17→18 | G2 | ✔
Нийт 11 DU хос. x@13→17 хос байхгүй, учир нь 17 нь x-г хэрэглэхгүй, зөвхөн тодорхойлно. tmp нь зөвхөн нэг def (16)-тэй.

A3. Тестийн тайлбар ба зааврын хариулт
- gcd(7,0) ганцаараа давталтад орохгүй. Тиймээс зөвхөн #2 (x@13→20) болон #5-ийн F салаа (y@13→15 false)-г хамарна.
- gcd(12,8) нь (12,8) → (8,4) → (4,0) гэж 2 давталт хийнэ. Давталт 1 дууссаны дараа x@17, y@18 үүсдэг тул #3, #8(T), #9, #10 хосууд дор хаяж 2 давталт шаарддаг.
- G1 ба G2 хоёр нийлээд 11 хосыг бүгдийг хамарна, өөрөөр хэлбэл all-DU-pairs хангагдсан.

B: bonus
B1. CFG
Зангилаа бүр файлын мөрийн дугаартай. Мөр 38 нь for-each тул хоёр зангилаатай: 38: hasNext(attendance) (attendance-ийн p-use) ба 38: a = next (a-ийн def). Ирмэг дээр тест бүрийн бодит метод нэр бичсэн (олон тест дайрдаг ирмэг дээр нэг-хоёр төлөөлөгч). Эх файл: cfg-bonus.dot.

B2. Def / Use жагсаалт
31: DEF attendance, threshold (параметр)
32: P-USE attendance, threshold (|| short-circuit: threshold нь attendance != null үед л үнэлэгдэнэ)
33: throw
35: DEF present
36: DEF streak
37: DEF best
38: P-USE attendance (hasNext) ; DEF a (дараагийн элемент)
39: P-USE a
40: C-USE present ; DEF present
41: C-USE streak ; DEF streak
42: P-USE streak, best
43: C-USE streak ; DEF best
46: DEF streak (KILL: streak@36, streak@41)
49: DEF score
50: P-USE present, threshold
51: DEF score (KILL: score@49)
53: P-USE best
54: C-USE score ; DEF score
56: C-USE score

B3. DU хосын хүснэгт
ID | Хувьсагч | Def | Use | Төрөл | Def-clear зам / тэмдэглэгээ | Тестийн ID
P1 | attendance | 31 | 32 | p (T/F) | 31→32 | T: nullAttendanceThrows; F: allDefs_longStreakThenBreak
P2 | attendance | 31 | 38 | p (T/F) | 31→32→35→38 | T: allDefs_longStreakThenBreak; F: allDefs_emptyBelowThreshold
P3 | threshold | 31 | 32 | p (T/F) | 31→32 | T: negativeThresholdThrows; F: allDefs_longStreakThenBreak
P4 | threshold | 31 | 50 | p (T/F) | 31→…→50 (threshold хэзээ ч дахин def болдоггүй) | T: allDefs_longStreakThenBreak; F: allDefs_emptyBelowThreshold
P5 | a | 38 | 39 | p (T/F) | 38→39 | T ба F: allDefs_longStreakThenBreak
P6 | present | 35 | 40 | c | 35→38→39→40 | allDefs_longStreakThenBreak
P7 | present | 35 | 50 | p (T/F) | 35→38→49→50 (хоосон массив) | T: emptyArrayThresholdZero; F: allDefs_emptyBelowThreshold
P8 | present | 40 | 40 | c | 40→41→42→38→39→40 | streakGrowsBeyondBest
P9 | present | 40 | 50 | p (T/F) | 40→…→38→49→50 | T: singleDayMeetsThreshold; F: singleDayBelowThreshold
P10 | streak | 36 | 41 | c | 36→38→39→40→41 (эхний элемент >0) | singleDayMeetsThreshold
P11 | streak | 41 | 41 | c | 41→42→38→39→40→41 (46-г дайрдаггүй) | streakGrowsBeyondBest
P12 | streak | 41 | 42 | p (T/F) | 41→42 | T: singleDayMeetsThreshold; F: streakDoesNotBeatBest
P13 | streak | 41 | 43 | c | 41→42(T)→43 | singleDayMeetsThreshold
P14 | streak | 46 | 41 | c | 46→38→39→40→41 | streakDoesNotBeatBest
P15 | best | 37 | 42 | p, зөвхөн T | 37→…→42; F биелэшгүй | singleDayMeetsThreshold
P16 | best | 37 | 53 | p, зөвхөн F | 37→38→49→50→53; T биелэшгүй | allDefs_emptyBelowThreshold
P17 | best | 43 | 42 | p (T/F) | 43→38→39→40→41→42 | T: streakGrowsBeyondBest; F: streakDoesNotBeatBest
P18 | best | 43 | 53 | p (T/F) | 43→38→49→50→53 | T: allDefs_longStreakThenBreak; F: singleDayMeetsThreshold
P19 | score | 49 | 54 | c | 49→50(F)→53(T)→54 | longStreakBelowThreshold
P20 | score | 49 | 56 | c | 49→50(F)→53(F)→56 | allDefs_emptyBelowThreshold
P21 | score | 51 | 54 | c | 51→53(T)→54 | allDefs_longStreakThenBreak
P22 | score | 51 | 56 | c | 51→53(F)→56 | emptyArrayThresholdZero
P23 | score | 54 | 56 | c | 54→56 | allDefs_longStreakThenBreak
Нийт 23 DU хос. Бүгд хамрагдсан.

Kill (def-ийг алдах) ба DU хос болохгүй хослолууд
- Мөр 46 streak = 0 нь streak@36 ба streak@41-ийг алддаг. Тиймээс 46-г дайрсан замаар P10, P11, P12, P13 def-clear болохгүй. Гэхдээ эдгээрт 46-г дайрдаггүй өөр зам байдаг (дараалсан эерэг өдрүүд) тул хосууд хүчинтэй хэвээр.
- Мөр 51 score = 5 нь score@49-ийг алддаг. Иймд P19, P20 нь зөвхөн 50 = F замаар def-clear.
DU хос БИШ (def-clear зам огт байхгүй) хослолууд:
- (streak, 36) → 42 ба (streak, 36) → 43
- (streak, 46) → 42 ба (streak, 46) → 43
Шалтгаан: 42 болон 43 руу орох бүх зам 41-ийг (streak = streak + 1) дайрдаг. 41 нь streak-ийг дахин тодорхойлдог тул 36 эсвэл 46-аас ирсэн утга 42/43-т хэзээ ч хүрэхгүй.
Биелэшгүй хос / салаа:
- P15 (best@37 → 42) F салаа: best нь 43-д л өөрчлөгддөг. 37-аас 42 хүртэл def-clear бол 43 ажиллаагүй, өөрөөр хэлбэл хамгийн анхны эерэг элемент. Тэр үед streak=1, best=0 тул 1 > 0 үргэлж true.
- P16 (best@37 → 53) T салаа: 43-т хүрээгүй бол best=0 хэвээр, 0 >= 5 үргэлж false.

B4. Тестүүд хоёр шатаар
1-р шат: ALL-DEFS — 2 тест
- allDefs_longStreakThenBreak: {1,1,1,1,1,0,1}, 3 → present@35, present@40, streak@36, streak@41, streak@46, best@37, best@43, score@51, score@54, a@38, attendance, threshold
- allDefs_emptyBelowThreshold: {}, 10 → score@49 → 56
Энэ шатны үр дүн: results/mvn-verify-alldefs.txt, results/jacoco-alldefs.csv.

2-р шат: ALL-DU-PAIRS — нэмсэн тестүүд
nullAttendanceThrows, negativeThresholdThrows, emptyArrayThresholdZero, singleDayMeetsThreshold, singleDayBelowThreshold, streakDoesNotBeatBest, streakGrowsBeyondBest, longStreakBelowThreshold, exactFiveExactThreshold. B3 хүснэгтийн "Тестийн ID" багана нь тус бүрийн хамарсан хосыг харуулна.

B5. JaCoCo ба ажиглалт
- results/jacoco-alldefs.csv: 2 тест (BonusTest), 16 branch-ээс 4 нь алдагдсан (BRANCH_MISSED=4), 12 нь хамрагдсан (BRANCH_COVERED=12). Алдагдсан 4-ийн 2 нь gcd-ийн while (тест хараахан байгаагүй), 2 нь bonus-ийнх.
- results/jacoco-final.csv: 16 branch = bonus 14 + gcd 2 (while). BRANCH_MISSED = 0 (16/16), LINE_MISSED = 1 (мөр 7, Java-гийн далд constructor).
All-defs хангагдсан ч branch 100% биж байж болох уу? Тийм. All-defs нь def бүрийг use-д ядаж нэг удаа хүргэхийг шаарддаг, харин p-use-ийн true/false салааг заавал хоёуланг нь шаарддаггүй.
Branch 100% болсон мөчид DU хос үлдэж болох уу? Тийм. Жишээ нь P19 (score,49)→(score,54) ба P22 (score,51)→(score,56) хосууд салаануудын хослол (combination) шаарддаг тул 4 үндсэн тестэд үлдсэн байсан.

B4 нэмэлт: all-defs яагаад 2 тест вэ
score-ийн def нь 49, 51, 54. score@49 нь 56-д хүрэхийн тулд 50 = F (мөн 53 = F) байх ёстой. score@51 үүсэхийн тулд 50 = T байх ёстой, мөн 51 нь score@49-ийг дарж бичдэг. Иймд score@49 ба score@51-ийг нэг гүйцэтгэлд use-д хүргэх боломжгүй, тиймээс 1 тест хүрэлцэхгүй. Хоёр тест (allDefs_longStreakThenBreak, allDefs_emptyBelowThreshold) бүх def-ийг хамарна, тиймээс 2 нь хамгийн цөөн.

B5 нэмэлт: branch 100% боловч DU хос үлдсэн жишээ
Дараах 4 тест bonus()-ийн 14 branch-ийг бүгдийг хамарна: (1) bonus(null, 0); (2) bonus({1}, -1); (3) bonus({1,1,1,1,1,0,1}, 3) -> 50=T, 53=T, 42 T/F, 39 T/F; (4) bonus({1,0,1}, 5) -> 50=F, 53=F. Гэвч P19 (score@49 -> 54, 50=F ба 53=T шаардана) ба P22 (score@51 -> 56, 50=T ба 53=F шаардана) хос хамрагдахгүй. Учир нь 50 ба 53-ийн (F,T), (T,F) хослол энэ 4 тестэд байхгүй. Эдгээрийг longStreakBelowThreshold (P19) ба emptyArrayThresholdZero (P22) тестүүд нэмж хамруулсан.

C: Fault seeding
- M1 (мөр 41 streak = streak + 1 устгасан): 3 тест унасан (M1 баригдсан).
- M2 (мөр 42–44 → best = present;): 13 тестээр амьд үлдсэн (DU хос дайрагдсан ч утга зөв эсэхийг шалгаагүйгээс болсон). M2KillerTest нэмсний дараа 14 тестээр баригдсан.
- M3 (давталтын дараа int total = present + best;): Үхсэн def (dead definition) тул ямар ч тест унагаагүй, харин VS Code (Java extension) "The value of the local variable total is not used" гэсэн static analysis анхааруулга өгсөн.

C нэмэлт: мутацийн үр дүнгийн хүснэгт
Мутаци | Төрөл | Тест (Tests run) | Унасан тест | expected / was | Үр дүн
M1 (мөр 41 устгасан) | Missing update | 13, Failures 3 | BonusTest.allDefs_longStreakThenBreak; BonusTest.longStreakBelowThreshold; BonusTest.exactFiveExactThreshold | expected: <8> but was: <5>; expected: <3> but was: <0>; expected: <8> but was: <5> | БАРИГДСАН
M2 (best = present) | Wrong variable | 13, Failures 0 | - | - | АМЬД ҮЛДСЭН
M2 + M2KillerTest | Wrong variable | 14, Failures 1 | M2KillerTest.presentFiveButStreakShort | expected: <5> but was: <8> | БАРИГДСАН
M3 (int total = present + best) | Dead definition | 13, Failures 0 | - | - | АМЬД ҮЛДСЭН (тест барьдаггүй)
(Эх сурвалж: results/mvn-test-M1.txt, mvn-test-M2.txt, mvn-test-M2-kill.txt, mvn-test-M3.txt)

M2 яагаад амьд үлдсэн бэ: all-DU-pairs тестүүдийн бүх тохиолдолд best шинэчлэгдэх мөчид present == streak байсан (цуваа тасраагүй, эсвэл present болон streak хоёр адил өсдөг). Тиймээс best = streak ба best = present ижил утга өгч, DU хос (present@40 -> 43 шиг use) дайрагдсан ч утга ялгарсангүй. M2KillerTest нь {1,1,0,1,1,0,1}, threshold 0 өгдөг: present = 5 боловч хамгийн урт цуваа = 2. Мутацитай код best = 5 болгож 53-д T өгч, 8 буцаана (зөв нь 5).
M3 яагаад унагадаггүй вэ: total хаана ч хэрэглэгдэхгүй тул bonus()-ийн буцаах утгад нөлөөлөхгүй. Ажиллах үеийн үр дүн хэвээр тул ямар ч тест унахгүй, javac ч анхааруулдаггүй. Static analysis (IDE-ийн unused variable анхааруулга: "The value of the local variable total is not used") энэ dead definition-ийг барина.

Эцсийн шалгалт
- git status-д DataFlowTargets.java өөрчлөгдсөн гэж гарахгүй.
- results/mvn-verify-final.txt: Tests run 14, Failures 0.
- results/jacoco-final.csv: BRANCH_MISSED = 0, LINE_MISSED = 1.

Дүгнэлт
DU анализ нь branch хамралтаас илүү нарийн бөгөөд утга хаанаас хаашаа урсаж байгааг хянадаг. Гэвч DU хамралт ч утгын зөв эсэхийг 100% гарантал шалгадаггүй (M2 шиг тохиолдол гардаг). Мөн үхсэн def (M3) зэрэг гажилтыг тест биш static analysis илрүүлдэг. DU анализ нь branch хамралтаас илүү, учир нь салаа бүрийг дайрсан эсэхээс гадна утга хаана тодорхойлогдож хаана хэрэглэгдэж байгааг шалгадаг. Бидний жишээнд 4 тест bonus()-ийн 14 branch-ийг бүгдийг хамарсан ч score-ийн 2 DU хос (P19, P22) үлдсэн. Гэхдээ DU хамралт ч бүрэн гарантитай биш: M2-ийг 23 хос хамрагдсан тестүүд барьж чадаагүй, учир нь хос дайрагдсан ч утга нь зөв эсэхийг шалгаагүй. DU анализ үнэтэй: def-clear замыг гараар олох, kill-ийг хянах, биелэшгүй хосыг (P15 F, P16 T) логикоор ялгах шаардлагатай бөгөөд JaCoCo энэ хамралтыг хэмждэггүй. Үхсэн def (M3) зэрэг зарим гажилтыг тест огт барьдаггүй, static analysis хэрэгтэй. Хамгийн их цаг авсан алхам нь B3, тухайлбал streak-ийн kill-үүдийг олж, 46 ба 41-ийн нөлөөгөөр аль хосууд хүчингүй болохыг тодорхойлох хэсэг. Мутацийн туршилтад mvn clean-гүйгээр ажиллуулбал мутаци compile хийгдэхгүй, M1 ч баригдаагүй мэт хуурамч үр дүн гардгийг ч мөн анзаарсан.
