

### 教学大纲

**1. Java 集合框架概述**
- 集合框架的基本概念
- 集合接口和类层次结构

**案例练习**：  
编写一个简单的程序展示 `Collection`、`List`、`Set`、`Map` 的基本用法。

**2. List 接口**
- `ArrayList` 和 `LinkedList`
- 操作方法：添加、删除、查找、迭代
- List 与数组的区别

**案例练习**：  
实现一个节目管理系统，用户可以添加节目名称，删除节目，或者查找某个节目是否存在，并展示节目列表。

**3. Set 接口**
- `HashSet`、`LinkedHashSet`、`TreeSet`
- 集合中元素的唯一性
- 常用操作和使用场景

**案例练习**：  
开发一个应用程序来管理学生的注册课程，确保每个学生的选课记录中不会有重复课程。

**4. Map 接口**
- `HashMap`、`LinkedHashMap`、`TreeMap`
- 键值对数据结构
- Map 的遍历方式

**案例练习**：  
创建一个简单的学生信息系统，通过学生学号查找学生的信息。

**5. 集合工具类**
- `Collections` 类和 `Arrays` 类的常用方法
- 集合排序、填充、反转等操作

**案例练习**：  
编写一个程序读取学生成绩，并按成绩排序输出前5名学生信息。

**6. 集合框架中的高级特性**
- 泛型的使用
- 集合框架中的流和过滤操作（Java 8）
- 并发集合（如 `ConcurrentHashMap`、`CopyOnWriteArrayList`）

**案例练习**：  
使用流 API 实现一个学生评价系统，筛选出90分以上的学生并计算平均分。

**7. 与集合相关的设计模式**
- 迭代器模式
- 观察者模式与事件监听

**案例练习**：  
使用迭代器模式设计一个简易的导航系统来遍历一系列地点信息。

---

### 总结

通过这个大纲，学生能够在学习 Java 集合框架的同时，通过实践练习来理解和应用所学知识。在教学过程中，建议采用互动方式，如小组讨论和代码评审，以增强学生的理解和协作能力。



---
当然，以下是一个简单的 Java 程序示例，展示了 `Collection`、`List`、`Set`、和 `Map` 的基本用法。每种数据结构都有一段示例代码来说明如何使用它们。

```java
import java.util.*;

public class CollectionDemo {
    public static void main(String[] args) {
        // Collection示例
        Collection<String> collection = new ArrayList<>();
        collection.add("Element 1");
        collection.add("Element 2");
        System.out.println("Collection: " + collection);
        
        // List示例
        List<String> list = new ArrayList<>();
        list.add("Item 1");
        list.add("Item 2");
        list.add("Item 1"); // List允许重复元素
        System.out.println("List: " + list);
        
        // Set示例
        Set<String> set = new HashSet<>();
        set.add("Unique 1");
        set.add("Unique 2");
        set.add("Unique 1"); // Set会自动去掉重复元素
        System.out.println("Set: " + set);
        
        // Map示例
        Map<String, Integer> map = new HashMap<>();
        map.put("Key1", 100);
        map.put("Key2", 200);
        map.put("Key1", 300); // Map会覆盖重复的键
        System.out.println("Map: " + map);
        
        // 遍历集合
        System.out.println("\n遍历 Collection:");
        for (String item : collection) {
            System.out.println(item);
        }

        System.out.println("\n遍历 List:");
        for (String item : list) {
            System.out.println(item);
        }

        System.out.println("\n遍历 Set:");
        for (String item : set) {
            System.out.println(item);
        }

        System.out.println("\n遍历 Map:");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
```

### 说明

- **Collection**：用于存储单一类型的数据元素，通过接口定义通用操作。
- **List**：一个有序集合，允许重复元素。典型实现包括 `ArrayList` 和 `LinkedList`。
- **Set**：一个无序集合，不允许重复元素。典型实现包括 `HashSet`、`LinkedHashSet` 和 `TreeSet`。
- **Map**：用于存储键值对的集合。允许根据键进行查找。典型实现包括 `HashMap`、`LinkedHashMap` 和 `TreeMap`。

### 遍历方式

- 使用 `for-each` 循环可以轻松地遍历 `Collection`、`List` 和 `Set`。
- `Map` 的遍历通常通过 `entrySet()` 方法进行，该方法返回包含键值对的 `Set`。

通过这个程序示例，可以很清楚地了解四种常用集合的基本用法。如果有其他特定需求或者更复杂的应用示例，欢迎继续交流！

---
以下是用 Java 实现的一个简单的节目管理系统。该系统使用 `ArrayList` 来管理节目名称，并提供添加、删除、查找和展示节目的功能。

```java
import java.util.ArrayList;
import java.util.Scanner;

public class ProgramManagementSystem {
    private ArrayList<String> programs;

    public ProgramManagementSystem() {
        this.programs = new ArrayList<>();
    }

    // 添加节目
    public void addProgram(String programName) {
        programs.add(programName);
        System.out.println("添加节目: " + programName);
    }

    // 删除节目
    public void removeProgram(String programName) {
        if (programs.remove(programName)) {
            System.out.println("删除节目: " + programName);
        } else {
            System.out.println("节目不存在: " + programName);
        }
    }

    // 查找节目
    public boolean hasProgram(String programName) {
        if (programs.contains(programName)) {
            System.out.println("节目存在: " + programName);
            return true;
        } else {
            System.out.println("节目不存在: " + programName);
            return false;
        }
    }

    // 展示节目列表
    public void showPrograms() {
        System.out.println("节目列表:");
        for (String program : programs) {
            System.out.println("- " + program);
        }
    }

    // 主方法，进行程序交互
    public static void main(String[] args) {
        ProgramManagementSystem system = new ProgramManagementSystem();
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("\n节目管理系统");
            System.out.println("1. 添加节目");
            System.out.println("2. 删除节目");
            System.out.println("3. 查找节目");
            System.out.println("4. 展示节目列表");
            System.out.println("5. 退出");
            System.out.print("选择操作: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // 处理换行符

            switch (choice) {
                case 1:
                    System.out.print("输入节目名称: ");
                    String addName = scanner.nextLine();
                    system.addProgram(addName);
                    break;
                case 2:
                    System.out.print("输入节目名称: ");
                    String removeName = scanner.nextLine();
                    system.removeProgram(removeName);
                    break;
                case 3:
                    System.out.print("输入节目名称: ");
                    String searchName = scanner.nextLine();
                    system.hasProgram(searchName);
                    break;
                case 4:
                    system.showPrograms();
                    break;
                case 5:
                    isRunning = false;
                    System.out.println("退出系统");
                    break;
                default:
                    System.out.println("无效选项，请重试");
            }
        }

        scanner.close();
    }
}
```

### 说明

- **数据结构**：使用 `ArrayList<String>` 来保存节目名称。
- **功能实现**：
    - `addProgram(String programName)`: 添加新节目。
    - `removeProgram(String programName)`: 删除节目，如果存在。
    - `hasProgram(String programName)`: 判断节目是否存在于列表中。
    - `showPrograms()`: 打印当前节目列表。
- **用户交互**：通过简单的控制台菜单让用户进行操作选择。

### 扩展

- 可以添加更多功能，例如对节目的排序、查找指定条件的节目等。
- 如果数据量较大或者并发访问场景，可以考虑使用更合适的集合或同步机制。

这个简单的管理系统为用户提供基础的节目增删查操作，并通过命令行互动方式进行操作。如果有需要进一步的优化或扩展，欢迎继续探讨！

---

要开发一个应用程序来管理学生的课程注册，确保没有重复的课程，可以使用 `HashMap` 和 `HashSet`。`HashMap` 可以用于将学生与他们的课程记录关联起来，而 `HashSet` 允许自动去除重复课程。以下是一个示例 Java 程序实现：

```java
import java.util.*;

public class CourseRegistrationSystem {
    private Map<String, Set<String>> studentCourses;

    public CourseRegistrationSystem() {
        studentCourses = new HashMap<>();
    }

    // 添加课程
    public void addCourse(String studentName, String courseName) {
        studentCourses.computeIfAbsent(studentName, k -> new HashSet<>());
        boolean added = studentCourses.get(studentName).add(courseName);
        if (added) {
            System.out.println("添加课程 '" + courseName + "' 至学生 '" + studentName + "'");
        } else {
            System.out.println("学生 '" + studentName + "' 已注册课程 '" + courseName + "'");
        }
    }

    // 删除课程
    public void removeCourse(String studentName, String courseName) {
        Set<String> courses = studentCourses.get(studentName);
        if (courses != null && courses.remove(courseName)) {
            System.out.println("从学生 '" + studentName + "' 移除课程 '" + courseName + "'");
        } else {
            System.out.println("学生 '" + studentName + "' 未注册课程 '" + courseName + "'");
        }
    }

    // 展示学生的课程
    public void showCourses(String studentName) {
        Set<String> courses = studentCourses.get(studentName);
        if (courses != null && !courses.isEmpty()) {
            System.out.println("学生 '" + studentName + "' 的课程列表:");
            for (String course : courses) {
                System.out.println("- " + course);
            }
        } else {
            System.out.println("学生 '" + studentName + "' 未注册任何课程");
        }
    }

    // 主方法，进行程序交互
    public static void main(String[] args) {
        CourseRegistrationSystem system = new CourseRegistrationSystem();
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("\n课程管理系统");
            System.out.println("1. 添加课程");
            System.out.println("2. 删除课程");
            System.out.println("3. 展示课程列表");
            System.out.println("4. 退出");
            System.out.print("选择操作: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // 处理换行符

            switch (choice) {
                case 1:
                    System.out.print("输入学生姓名: ");
                    String studentName = scanner.nextLine();
                    System.out.print("输入课程名称: ");
                    String courseName = scanner.nextLine();
                    system.addCourse(studentName, courseName);
                    break;
                case 2:
                    System.out.print("输入学生姓名: ");
                    studentName = scanner.nextLine();
                    System.out.print("输入课程名称: ");
                    courseName = scanner.nextLine();
                    system.removeCourse(studentName, courseName);
                    break;
                case 3:
                    System.out.print("输入学生姓名: ");
                    studentName = scanner.nextLine();
                    system.showCourses(studentName);
                    break;
                case 4:
                    isRunning = false;
                    System.out.println("退出系统");
                    break;
                default:
                    System.out.println("无效选项，请重试");
            }
        }

        scanner.close();
    }
}
```

### 说明

- **数据结构**：
    - `HashMap<String, Set<String>> studentCourses`: 将学生姓名映射到他们已注册的课程集合（`HashSet`）。
    - `HashSet` 用于存储课程名称，确保无重复。

- **功能实现**：
    - `addCourse(String studentName, String courseName)`: 在给定学生的课程列表中添加课程。
    - `removeCourse(String studentName, String courseName)`: 从给定学生的课程列表中移除课程。
    - `showCourses(String studentName)`: 显示学生的所有注册课程。

- **用户交互**：简单的控制台菜单操作。

### 扩展

- 可以增加对课程信息的详细描述（如学分、教师等）。
- 考虑使用文件或数据库存储学生和课程数据以实现持久化。

通过这个示例程序，能够有效管理学生的注册课程记录，并避免重复。若有其他功能需求或优化建议，欢迎继续探讨交流！

---

要创建一个简单的学生信息系统，可以使用 `HashMap` 来管理和查询学生信息。`HashMap` 非常适合这种键值对查询的场景。以下是一个简单的 Java 实现示例：

```java
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Student {
    private String id;
    private String name;
    private int age;
    private String department;

    public Student(String id, String name, int age, String department) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.department = department;
    }

    @Override
    public String toString() {
        return "学号: " + id + ", 姓名: " + name + ", 年龄: " + age + ", 院系: " + department;
    }
}

public class StudentInformationSystem {
    private Map<String, Student> studentMap;

    public StudentInformationSystem() {
        studentMap = new HashMap<>();
    }

    // 添加学生信息
    public void addStudent(String id, String name, int age, String department) {
        Student student = new Student(id, name, age, department);
        studentMap.put(id, student);
        System.out.println("新增学生: " + student);
    }

    // 查找学生信息
    public void findStudent(String id) {
        Student student = studentMap.get(id);
        if (student != null) {
            System.out.println("查询结果: " + student);
        } else {
            System.out.println("未找到学号为 " + id + " 的学生");
        }
    }

    // 主方法，进行程序交互
    public static void main(String[] args) {
        StudentInformationSystem system = new StudentInformationSystem();
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("\n学生信息系统");
            System.out.println("1. 新增学生信息");
            System.out.println("2. 查找学生信息");
            System.out.println("3. 退出");
            System.out.print("选择操作: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // 处理换行符

            switch (choice) {
                case 1:
                    System.out.print("输入学号: ");
                    String id = scanner.nextLine();
                    System.out.print("输入姓名: ");
                    String name = scanner.nextLine();
                    System.out.print("输入年龄: ");
                    int age = scanner.nextInt();
                    scanner.nextLine();  // 处理换行符
                    System.out.print("输入院系: ");
                    String department = scanner.nextLine();
                    system.addStudent(id, name, age, department);
                    break;
                case 2:
                    System.out.print("输入学号: ");
                    id = scanner.nextLine();
                    system.findStudent(id);
                    break;
                case 3:
                    isRunning = false;
                    System.out.println("退出系统");
                    break;
                default:
                    System.out.println("无效选项，请重试");
            }
        }

        scanner.close();
    }
}
```

### 说明

- **数据结构**：
    - `HashMap<String, Student> studentMap`: 用于将学生学号映射到学生对象。
    - `Student` 类：包含学生的基本信息，如学号、姓名、年龄和院系。

- **功能实现**：
    - `addStudent(String id, String name, int age, String department)`: 添加新的学生信息到系统中。
    - `findStudent(String id)`: 根据学号查询学生信息。

- **用户交互**：通过简单的命令行菜单操作进行交互。

### 扩展

- 增加数据验证，以确保输入的数据格式正确。
- 使用文件或数据库进行数据持久化，以保存学生信息。

这个示例系统能够有效管理和查询学生信息，通过学号进行快速访问。如果有更多功能需求或优化建议，欢迎继续讨论交流！

---
以下是一个简单的程序实现，用于读取学生成绩，并按成绩排序输出前5名学生的信息。我们可以使用 `ArrayList` 和 `Collections.sort()` 进行排序。

```java
import java.util.*;

class Student {
    private String name;
    private double score;

    public Student(String name, double score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public double getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "学生: " + name + ", 成绩: " + score;
    }
}

public class StudentScoreSorter {
    private List<Student> students;

    public StudentScoreSorter() {
        this.students = new ArrayList<>();
    }

    // 增加学生成绩
    public void addStudentScore(String name, double score) {
        students.add(new Student(name, score));
    }

    // 输出前5名学生信息
    public void printTopStudents() {
        Collections.sort(students, Comparator.comparingDouble(Student::getScore).reversed());
        System.out.println("前5名学生信息:");
        for (int i = 0; i < Math.min(5, students.size()); i++) {
            System.out.println(students.get(i));
        }
    }

    public static void main(String[] args) {
        StudentScoreSorter sorter = new StudentScoreSorter();
        Scanner scanner = new Scanner(System.in);

        System.out.print("输入学生数量: ");
        int numStudents = scanner.nextInt();
        scanner.nextLine(); // 处理换行符

        for (int i = 0; i < numStudents; i++) {
            System.out.print("输入学生姓名: ");
            String name = scanner.nextLine();
            System.out.print("输入学生成绩: ");
            double score = scanner.nextDouble();
            scanner.nextLine(); // 处理换行符
            sorter.addStudentScore(name, score);
        }

        sorter.printTopStudents();
        scanner.close();
    }
}
```

### 说明

- **数据结构**：
    - 使用 `ArrayList<Student>` 来保存学生和对应的成绩。

- **排序实现**：
    - 使用 `Collections.sort()` 与 `Comparator` 按成绩逆序排序，确保高分在前。
    - `Comparator.comparingDouble(Student::getScore).reversed()` 用于按成绩逆序排列。

- **功能实现**：
    - `addStudentScore(String name, double score)`: 添加学生成绩到列表中。
    - `printTopStudents()`: 排序并打印前5名学生信息。

### 扩展

- 增加数据验证，确保输入的成绩是合理的范围。
- 提供从文件或其他输入源读取数据的功能。

这个示例程序有效地展示了如何排序和输出前5名学生的信息，在处理成绩时保持简单和高效。如果有其他特定需求或优化建议，欢迎继续探讨交流！

---

使用 Java 流 API 进行数据过滤和计算是一个非常优雅和现代的方法。在这个示例中，我们将展示如何使用流 API 来实现一个学生评价系统，以筛选出成绩在 90 分以上的学生并计算他们的平均分。

```java
import java.util.*;
import java.util.stream.Collectors;

class Student {
    private String name;
    private double score;

    public Student(String name, double score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public double getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "学生: " + name + ", 成绩: " + score;
    }
}

public class StudentEvaluationSystem {
    private List<Student> students;

    public StudentEvaluationSystem() {
        this.students = new ArrayList<>();
    }

    // 增加学生成绩
    public void addStudentScore(String name, double score) {
        students.add(new Student(name, score));
    }

    // 筛选并计算平均分
    public void evaluateAndPrintTopStudents() {
        List<Student> topStudents = students.stream()
                .filter(student -> student.getScore() > 90)
                .collect(Collectors.toList());

        System.out.println("90分以上的学生:");
        topStudents.forEach(System.out::println);

        double averageScore = topStudents.stream()
                .mapToDouble(Student::getScore)
                .average()
                .orElse(0);

        System.out.println("这些学生的平均分: " + averageScore);
    }

    public static void main(String[] args) {
        StudentEvaluationSystem system = new StudentEvaluationSystem();
        Scanner scanner = new Scanner(System.in);

        System.out.print("输入学生数量: ");
        int numStudents = scanner.nextInt();
        scanner.nextLine(); // 处理换行符

        for (int i = 0; i < numStudents; i++) {
            System.out.print("输入学生姓名: ");
            String name = scanner.nextLine();
            System.out.print("输入学生成绩: ");
            double score = scanner.nextDouble();
            scanner.nextLine(); // 处理换行符
            system.addStudentScore(name, score);
        }

        system.evaluateAndPrintTopStudents();
        scanner.close();
    }
}
```

### 说明

- **流 API 使用**：
    - `students.stream().filter(...)`：过滤出成绩大于 90 的学生，结果存入 `topStudents` 列表。
    - `topStudents.stream().mapToDouble(...)`: 将 `topStudents` 列表中的成绩转换为 `double` 流，并计算平均值。

- **收集与计算**：
    - 使用 `Collectors.toList()` 来收集过滤后的学生。
    - 使用 `average()` 对 `double` 流进行计算，若没有学生成绩超过90分，则默认平均值为0。

- **代码的优雅性**：
    - 简洁且具有较强的表达能力。
    - 使用 `orElse(0)` 处理无结果情况，确保 `average` 的返回结果始终可用。

### 扩展

- 增加功能来处理不同的科目平均分统计。
- 提供数据持久化或从文件读取数据的支持。

通过流 API 可以简化许多集合操作，继而使代码更加自然和易读。如果有更多特定需求或优化建议，欢迎继续探讨交流！

---

在 Java 的集合框架中，了解高级特性可以帮助我们开发更高效、更安全的应用程序。以下是这些高级特性的详细解释和示例：

---

### 1. 泛型的使用

**目的**：泛型允许集合在使用时指定类型，提供了类型安全和避免强制类型转换的优势。

**示例**：

```java
import java.util.ArrayList;
import java.util.List;

public class GenericsDemo {
    public static void main(String[] args) {
        // 使用泛型指定类型为 String
        List<String> names = new ArrayList<>();
        names.add("Alice");
        names.add("Bob");

        // 在添加元素时和获取元素时均不需要显式类型转换
        for (String name : names) {
            System.out.println(name);
        }
    }
}
```

### 2. 集合框架中的流和过滤操作（Java 8）

**目的**：流 API 提供了一种简洁、高效的处理集合的方式，能够对元素进行过滤、排序、统计等操作。

**示例**：

```java
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StreamDemo {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");

        // 过滤名字长度大于三的并打印
        List<String> filteredNames = names.stream()
                .filter(name -> name.length() > 3)
                .collect(Collectors.toList());

        filteredNames.forEach(System.out::println);
    }
}
```

### 3. 并发集合

**目的**：在多线程环境下，普通集合类不保证线程安全，并发集合类使得在并发操作时具备更好的性能和安全性。

**常见的并发集合**：

- **ConcurrentHashMap**：一个线程安全的哈希表，适合频繁的读操作。
- **CopyOnWriteArrayList**：适合读取多于写入的场景，每次写入时都会复制原列表以保证线程安全。

**示例**：

```java
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentCollectionDemo {
    public static void main(String[] args) {
        ConcurrentHashMap<String, Integer> concurrentMap = new ConcurrentHashMap<>();

        // Put key-value pairs
        concurrentMap.put("key1", 1);
        concurrentMap.put("key2", 2);

        // Concurrent access by threads
        Runnable readTask = () -> System.out.println("Value for key1: " + concurrentMap.get("key1"));
        Runnable writeTask = () -> concurrentMap.put("key1", 10);

        Thread thread1 = new Thread(readTask);
        Thread thread2 = new Thread(writeTask);

        thread1.start();
        thread2.start();
    }
}
```

### 总结

- **泛型**提高了类型安全性和代码的可读性。
- **流 API**使得集合操作更加简洁和高效，尤其适合处理大量数据。
- **并发集合**是多线程安全集合的良好选择，提高程序的扩展性和稳定性。

这些高级特性使得 Java 集合能够处理复杂的应用场景，帮助开发者设计性能优秀和安全可靠的软件。如果有其他相关问题或需要深入的案例探讨，请继续交流！

---

迭代器模式是一种设计模式，用于提供一种方法顺序访问一个聚合对象中的各个元素，而不暴露其内部表示。我们可以用迭代器模式设计一个简易的导航系统，以遍历一系列地点信息。

下面是一个简单的示例程序，展示如何使用迭代器模式来实现一个导航系统：

```java
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// 定义地点类
class Location {
    private String name;

    public Location(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}

// 定义地点集合类
class LocationCollection implements Iterable<Location> {
    private List<Location> locations;

    public LocationCollection() {
        this.locations = new ArrayList<>();
    }

    public void addLocation(Location location) {
        locations.add(location);
    }

    @Override
    public Iterator<Location> iterator() {
        return locations.iterator();
    }
}

// 导航系统类使用迭代器遍历地点信息
public class NavigationSystem {
    public static void main(String[] args) {
        LocationCollection collection = new LocationCollection();
        collection.addLocation(new Location("博物馆"));
        collection.addLocation(new Location("公园"));
        collection.addLocation(new Location("剧院"));
        collection.addLocation(new Location("餐厅"));

        // 使用迭代器遍历地点
        Iterator<Location> iterator = collection.iterator();
        System.out.println("导航系统正在遍历地点信息:");
        while (iterator.hasNext()) {
            Location location = iterator.next();
            System.out.println("现在位置: " + location);
        }
    }
}
```

### 说明

- **地点类（Location）**：表示一个导航地点，有名称属性。
- **地点集合类（LocationCollection）**：管理一组地点，实现 `Iterable<Location>` 接口，以支持迭代。
- **迭代器接口**：地点集合类通过实现 `Iterable<Location>` 来提供 `iterator()` 方法，使得可以使用增强的 `for-each` 循环或显式迭代器。

### 扩展思考

- **支持更多操作**：导航系统可以扩展以支持动态添加和删除地点。
- **导航逻辑**：可以将导航逻辑包含在单独的类中实现不同的导航策略或路径规划。
- **观察者模式**：可以考虑加入观察者模式（例如用户选择兴趣点变化）以响应事件变化。

通过迭代器模式，我们能够以一种统一的方法访问复杂集合中的元素，同时保持设计的简洁和灵活性。如果有其他相关功能或模式需要探讨，欢迎继续交流！




