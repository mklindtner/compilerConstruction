import java.util.HashMap;
import java.util.List;

abstract class AST {
    abstract public Value eval(Environment env, FnEnvironment fenv);
};

abstract class Expr extends AST {
    // abstract public Value eval(Environment env);
}

// abstract class Expr extends AST {
// abstract public Value eval(Environment env, Environment Env);
// }

enum JavaType {
    DOUBLETYPE, BOOLTYPE
}

class faux { // collection of non-OO auxiliary functions (currently just error)
    public static final String err_suffix = " Should not evaluate";
    public static final String err_value_suffix = " Cannot evaluate that type";
    public static void error(String msg) {
        System.err.println("compiler error: " + msg);
        System.exit(-1);
    }
}

class Value {
    public JavaType valueType;
    public Double d;
    public boolean b;

    Value(Double d) {
        this.valueType = JavaType.DOUBLETYPE;
        this.d = d;
    }

    Value(boolean b) {
        this.valueType = JavaType.BOOLTYPE;
        this.b = b;
    }

    public String toString() {
        if (valueType == JavaType.DOUBLETYPE) {
            return "" + d;
        }
        if (valueType == JavaType.BOOLTYPE) {
            return "" + b;
        }
        System.out.println("valueType is not defined");
        return null;
    }
}


class Start extends AST {
    List<Fn> fns;
    Expr expr;

    Start(List<Fn> fns, Expr expr) {
        this.fns = fns;
        this.expr = expr;
    }

    public Value eval(Environment env, FnEnvironment fnEnv) {
        faux.error("start" + faux.err_suffix);
        return null;
    }
}
class TypeIdentifier extends AST {
    public JavaType type;
    public String identifier;

    TypeIdentifier(JavaType type, String identifier) {
        this.type = type;
        this.identifier = identifier;
    }

    public Value eval(Environment env, FnEnvironment fnEnv) {
        faux.error("TypeIdentifier " + faux.err_suffix);
        return null;
    }
}

class Fn extends AST {
    public TypeIdentifier tId;
    public List<TypeIdentifier> params;
    public Expr expr;

    Fn(TypeIdentifier tId, List<TypeIdentifier> params, Expr e) {
        this.tId = tId;
        this.params = params;
        this.expr = e;
    }

    public Value eval(Environment env, FnEnvironment fnEnv) {
        faux.error("function" + faux.err_suffix);
        return null;
    }
}

class FnCall extends Expr {
    public List<Expr> exprs;
    public String fnName;

    FnCall(String fnName, List<Expr> exprs) {
        this.fnName = fnName;
        this.exprs = exprs;
    }

    public Value eval(Environment env, FnEnvironment fnEnv) {
        faux.error("FnCall" + faux.err_suffix);
        return null;
    }
}

class FnEnvironment {
	private HashMap<String, Fn> fns = new HashMap<>();

	public FnEnvironment() {
	}

	public void setFunction(String name, Fn f) {
		fns.put(name, f);
	}

	public Fn getFunction(String name) {
		Fn f = fns.get(name);
		if (f == null)
			faux.error("Function not defined: " + name);
		return f;
	}
}

//move Env here

class UnaryMinus extends Expr {
    Expr e1;

    UnaryMinus(Expr e1) {
        this.e1 = e1;
    }

    public Value eval(Environment env, FnEnvironment fnEnv) {
        Value val = e1.eval(env, fnEnv);
        if(val.valueType == JavaType.DOUBLETYPE) 
        {
            System.out.println("eval: " + val);

            return new Value(-1 * val.d);
        }
        System.out.println(""+val.valueType+faux.err_value_suffix);
        return null;
    }
}

class MultDiv extends Expr {
    Expr e1, e2;
    String op;

    MultDiv(String op, Expr e1, Expr e2) {
        this.op = op;
        this.e1 = e1;
        this.e2 = e2;
    }

    public Value eval(Environment env, FnEnvironment fnEnv) {
        Value val1 = e1.eval(env, fnEnv);
        Value val2 = e2.eval(env, fnEnv);
        if(val1.valueType == JavaType.DOUBLETYPE && val2.valueType == JavaType.DOUBLETYPE)
        {
            if(op.equals("*"))
            {
                return new Value(val1.d * val2.d);
            }
            return new Value(val1.d / val2.d);
        }        
        System.out.println(""+val1.valueType+" with " + val2.valueType + faux.err_value_suffix);      
        return null;
    }
}

class Sub extends Expr {
    Expr e1, e2;

    Sub(Expr e1, Expr e2) {
        this.e1 = e2;
        this.e2 = e2;
    }

    public Value eval(Environment env, FnEnvironment fnEnv) {
        // System.out.println("inside SUB\tenv:" + env);
        Value val1 = e1.eval(env, fnEnv);
        Value val2 = e2.eval(env, fnEnv);
        if(val1.valueType == JavaType.DOUBLETYPE && val2.valueType == JavaType.DOUBLETYPE)
        {
            return new Value(val1.d - val1.d);
            // return e1.eval(env) - e2.eval(env);
        }
        System.out.println(""+val1.valueType+" with " + val2.valueType + faux.err_value_suffix);      
        return null;
    }
}

class Add extends Expr {
    Expr e1, e2;

    Add(Expr e1, Expr e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public Double eval(Environment env) {
        System.out.println("inside ADD env:");
        return e1.eval(env) + e2.eval(env);
    }
}

class Constant extends Expr {
    Double d;

    Constant(Double d) {
        this.d = d;
    }

    public Double eval(Environment env) {
        return d;
    }
}

class Variable extends Expr {
    String varname;

    Variable(String varname) {
        this.varname = varname;
    }

    public Double eval(Environment env) {
        return env.getVariable(varname);
    }
}

abstract class fun extends AST {
    abstract public void eval(Environment env);
}

abstract class Command extends AST {
    abstract public void eval(Environment env);
}

// Do nothing command
class NOP extends Command {
    public void eval(Environment env) {
    };
}

class Sequence extends Command {
    Command c1, c2;

    Sequence(Command c1, Command c2) {
        this.c1 = c1;
        this.c2 = c2;
    }

    public void eval(Environment env) {
        c1.eval(env);
        c2.eval(env);
    }
}

class Assignment extends Command {
    String v;
    Expr e;

    Assignment(String v, Expr e) {
        this.v = v;
        this.e = e;
    }

    public void eval(Environment env) {
        Double d = e.eval(env);
        env.setVariable(v, d);
    }
}

class Output extends Command {
    Expr e;

    Output(Expr e) {
        this.e = e;
    }

    public void eval(Environment env) {
        Double d = e.eval(env);
        System.out.println(d);
    }
}

class While extends Command {
    Condition c;
    Command body;

    While(Condition c, Command body) {
        this.c = c;
        this.body = body;
    }

    public void eval(Environment env) {
        while (c.eval(env))
            body.eval(env);
    }
}

abstract class Condition extends AST {
    abstract public Boolean eval(Environment env);
}

class Unequal extends Condition {
    Expr e1, e2;

    Unequal(Expr e1, Expr e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public Boolean eval(Environment env) {
        return !e1.eval(env).equals(e2.eval(env));
    }

}
