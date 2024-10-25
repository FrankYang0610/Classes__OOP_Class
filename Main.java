interface I {
    /*
     * All fields in the interface are public [static] final!
     * All methods in the interface are public abstract!
     */

    // private int a; /* WRONG! No private, and the field must be initialized! */

    int a = 0; /* OK */

    public void f(); /* OK! */

    // public void f(int x) {} /* WRONG! No implementation in an interface! */

    public void g();

    default public void g(int x) {
        System.out.println(x);
    }

    // private void h(); /* WRONG! */
}

abstract interface J {} /* OK, but 'abstract' can be deleted */

interface K extends I, J { /* OK! */
    interface T {} /* OK! */
    interface U extends K {} /* OK! */
    class V {} /* OK */
}

// interface L implements I {} /* WRONG! */

interface M extends K {
    @Override
    public void f(); /* OK! */

    public void g(); /* Also an overriding */

    public void g(int x); /* Overloading */
}

// final interface N extends M {} /* WRONG! An interface can't be final! */

class A {
    public static int a;

    protected int b;

    private int c;

    public static A s = new A(); /* s represents 'staticInstance' */

    public A() {}

    public static void f() {}

    public void g() {}

    protected void g(int x) {} /* Overloading */

    private void h() {}

    //public void h() {} /* WRONG, method signature can't be the same! */

    public final void j() {}

    public final void j(double x) {} /* OK! final methods can be overloaded. */

    public void j(boolean x) {} /* OK! Another overloading */
}

class B extends A { /* In all instances of this class, there will be a space for fields from A, including private int c. */
    // @Override
    // public A() {} /* WRONG! Can't override super class's constructor! */

    public void A() {} /* OK! This is an ordinary method, not a constructor */

    // @Override /* WRONG, overriding is a runtime polymorphism, so static fields / methods can't be overridden! */
    public static void f() {} /* Method hiding */

    public static void f(int x) {
        // super.f(); /* WRONG! Need to make this method not static! */
        A.f(); /* OK! */
        // this.f(); /* WRONG! Need to make this method not static! */
        f(); B.f(); /* OK! */
    }

    public void f(double x) {} /* Valid function overloading */

    public void f(boolean x) {
        super.f(); this.f(); /* OK, but not recommended */
        A.f(); f(); B.f(); /* OK! */
    }

    @Override public void g() {
        super.g(); /* OK! */
        System.out.println("B.g() called!");
    }

    // @Override private void g(int x) {} /* WRONG, access modifier should not be stricter than protected */

    // @Override /* WRONG */
    public void h() {}

    public void h(int t) {} /* Overloading */

    // @Override
    // public final void j() {} /* WRONG, cannot override a final method from a superclass */

    // public final void j() {} /* WRONG, even if no @Override is added, it is actually [overriding] */

    // private final void j() {} /* WRONG, even if no @Override is added, it is actually [overriding] */

    public final void j(int x) { super.j(); } /* OK! */
}

/* Another class in the same package */
class C {
    public A i; /* i represents 'instance' */
    public C() { System.out.println(i.b); /* OK! */ }
}

abstract class D { /* D can't be instantiated directly, any instantiation must through the inheritance! */
    public D() {} /* OK! */

    public void f() {} /* OK! */

    public void f(int x) {} /* OK! */

    public void g() {
        System.out.println("D.g() called!"); /* OK! */
    }

    public static void h() {}
}

class E extends D { /* D is instantiated in E */
    @Override
    public void f() {} /* OK! */

    @Override
    public void g() {
        // D.g(); /* WRONG! Not static! */
        super.g(); /* OK! */
        System.out.println("E.g() called!");
    }

    // @Override /* WRONG! */
    public static void h() {}
}

abstract class F extends D {} /* OK! */

// final abstract class G {} /* WRONG! No final abstract classes! */

final class G {}

// final class H extends G {} /* WRONG! final classes can't be inherited! */

sealed class P permits Q, R {}

final class Q extends P {}

// class R extends P {} /* WRONG! */
// non-sealed class R extends P {} /* OK! */
// sealed class R permits ... extends P {} /* OK! */
final class R extends P {} /* OK! */

public class Main {
    public static void main(String[] args) {
        A a1 = new B(); /* OK! */
        a1.g(); /* Runtime overriding, output 'B.g() called!' */

        // B b1 = new A(); /* Compiletime error! Can't directly downcasting! */
        // B b1 = (B)new A(); /* Runtime error! ClassCastException! */

        A a2 = new A();
        B b2 = null;


        /*
         * operator instanceof and class casting are executed during runtime,
         * type safety is being executed during compiletime.
         */

        if (a2 instanceof B) { /* This won't be called! */
            System.out.println("d = (B)c will be called!");
            b2 = (B)a2; /* ClassCastException! */
        }

        if (a1 instanceof B) { /* This will be called! */
            System.out.println("d = (B)a will be called!");
            // d = a; /* WRONG! The reference type should be cast */
            b2 = (B)a1; /* OK! */
        }

        // D d = new D(); /* Abstract classes can't be instantiated! */

        E e = new E();
        e.g();
    }
}
