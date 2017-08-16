package com.test;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observables.GroupedObservable;
import rx.schedulers.Schedulers;

/**
 * Created by cwj on 17/5/16.
 */

public class TestRXJava {

    public static void main(String[] args) {
//        testStrings();
//        testError();
//        testMap();
//        testFlatMap();
//        testMultiTransorm();
        testSchedulers();
        testGroup();
    }

    private static void testGroup() {
        Observable.from(new Object[]{1, "a", 2, "b", 3, "c"})
                .filter(new Func1<Object, Boolean>() {
                    @Override
                    public Boolean call(Object o) {
                        return o != null;
                    }
                })
                .groupBy(new Func1<Object, String>() {
                    @Override
                    public String call(Object o) {
                        return o.getClass().getSimpleName();
                    }
                }, new Func1<Object, String>() {
                    @Override
                    public String call(Object o) {
                        return o.getClass().getSimpleName();
                    }
                }).subscribe(new Action1<GroupedObservable<String, String>>() {
            @Override
            public void call(GroupedObservable<String, String> stringStringGroupedObservable) {
                String key = stringStringGroupedObservable.getKey();
                if (String.class.getSimpleName().equals(key)) {
                    stringStringGroupedObservable.subscribe(new Action1<String>() {
                        @Override
                        public void call(String o) {
                        }
                    });
                } else if (Integer.class.getSimpleName().equals(key)) {
                    stringStringGroupedObservable.subscribe(new Action1<String>() {
                        @Override
                        public void call(String o) {
                        }
                    });
                }
            }
        });
    }

    private static void testMultiTransorm() {
        Observable.from(getStudents())
                .flatMap(new Func1<Student, Observable<Course>>() {
                    @Override
                    public Observable<Course> call(Student student) {
                        return Observable.from(student.courses);
                    }
                }).map(new Func1<Course, String>() {
            @Override
            public String call(Course course) {
                return course.name;
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("multi:" + s);
            }
        });
    }

    private static void testSchedulers() {
        Observable.from(getStudents())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        System.out.println("start");
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .map(new Func1<Student, String>() {
                    @Override
                    public String call(Student student) {
                        return student.name;
                    }
                })
                .observeOn(Schedulers.newThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println("map:" + s);
                    }
                });
    }

    private static void testFlatMap() {
        Observable.from(getStudents())
                .flatMap(new Func1<Student, Observable<Course>>() {
                    @Override
                    public Observable<Course> call(Student student) {
                        return Observable.from(student.courses);
                    }
                })
                .subscribe(new Action1<Course>() {
                    @Override
                    public void call(Course course) {
                        System.out.println("flatMap:" + course.name);
                    }
                });
    }

    private static void testMap() {
        Observable.from(getStudents())
                .map(new Func1<Student, String>() {
                    @Override
                    public String call(Student student) {
                        return student.name;
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println("map:" + s);
                    }
                });
    }

    private static Student[] getStudents() {
        Course[] courses1 = {new Course("语文"), new Course("数学"), new Course("英语")};
        Course[] courses2 = {new Course("体育"), new Course("美术"), new Course("物理")};
        return new Student[]{new Student("cwj", courses1), new Student("zdm", courses2)};
    }

    private static void testError() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                String[] strs = {"s", "", "str"};
                for (String s : strs) {
                    if (s == null || s.length() == 0) {
                        subscriber.onError(new IllegalArgumentException("string length cannot be 0"));
                    } else {
                        subscriber.onNext(s.length());
                    }
                }
                subscriber.onCompleted();
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                System.out.println("complete");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("异常：" + e.getMessage());
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("str length is " + integer);
            }
        });
    }

    private static void testStrings() {
        String[] names = {"str1", "str2", "str3", "str4"};
        Observable.from(names)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println("I am " + s);
                    }
                });
    }
}

class Student {
    public final String name;
    public final Course[] courses;

    Student(String name, Course[] courses) {
        this.name = name;
        this.courses = courses;
    }
}

class Course {
    public final String name;

    Course(String name) {
        this.name = name;
    }
}