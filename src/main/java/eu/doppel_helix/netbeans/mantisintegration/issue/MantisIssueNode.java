
package eu.doppel_helix.netbeans.mantisintegration.issue;

import biz.futureware.mantisconnect.ObjectRef;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Calendar;
import org.netbeans.modules.bugtracking.issuetable.IssueNode;

public class MantisIssueNode extends IssueNode<MantisIssue> {

    public MantisIssueNode(MantisIssue issue) {
        super(issue.getMantisRepository().getRepository(), issue);
    }
    
    @Override
    protected Property<?>[] getProperties() {
        try {
            return new Property<?>[] {
                new ReflectionProperty<BigInteger>("mantis.issue.id", "ID", "Identifier", getIssueData(), BigInteger.class, "getId", null),
                new SummaryProperty(),
                new ReflectionProperty<Integer>("mantis.issue.noteCount", "#", "Note count", getIssueData(), Integer.class, "getNoteCount", null),
                new ReflectionProperty<String>("mantis.issue.category", "Category", "Category", getIssueData(), String.class, "getCategory", null),
                new ReflectionProperty<ObjectRef>("mantis.issue.severity", "Severity", "Severity", getIssueData(), ObjectRef.class, "getSeverity", null),
                new ReflectionProperty<ObjectRef>("mantis.issue.status", "Status", "Status", getIssueData(), ObjectRef.class, "getStatus", null),
                new ReflectionProperty<Calendar>("mantis.issue.updated", "Updated", "Updated", getIssueData(), Calendar.class, "getLast_updated", null),    
            };
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private class ReflectionProperty<T> extends IssueNode<MantisIssue>.IssueProperty<T> {
        private Object instance;
        private Class<? extends T> valueType;
        private Method getter;
        private Method setter;
        
        public ReflectionProperty(String name, String displayName, String shortDescription, Object instance, Class<T> valueType, String property) throws NoSuchMethodException {
            super(name, valueType, displayName, shortDescription);
            this.instance = instance;
            this.valueType = valueType;
            String accessorBase = property.substring(0, 1).toUpperCase() + property.substring(1);
            this.setter = instance.getClass().getMethod("set" + accessorBase, new Class[] {valueType});
            try {
                this.getter = instance.getClass().getMethod("get" + accessorBase, new Class[] {});
            } catch (NoSuchMethodException ex) {
                this.getter = instance.getClass().getMethod("is" + accessorBase, new Class[] {});
            }
            this.setName(name);            
        }

        public ReflectionProperty(String name, String displayName, String shortDescription, Object instance, Class<T> valueType, Method getter, Method setter) {
            super(name, valueType, displayName, shortDescription);
            this.instance = instance;
            this.valueType = valueType;
            this.setter = setter;
            this.getter = getter;
            this.setName(name);
        }

        public ReflectionProperty(String name, String displayName, String shortDescription, Object instance, Class<T> valueType, String getter, String setter) throws NoSuchMethodException {
            super(name, valueType, displayName, shortDescription);
            this.instance = instance;
            this.valueType = valueType;
            this.setter = null;
            if(setter != null) {
                this.setter = instance.getClass().getMethod(setter, new Class[] {valueType});
            }
            this.getter = null;
            if(getter != null) {
                this.getter = instance.getClass().getMethod(getter, new Class[] {});
            }
            this.setName(name);
        }

        @Override
        public String toString() {
            if(getValue() == null) {
                return "";
            } else {
                return getValue().toString();
            }
        }

        @Override
        public T getValue() {
            try {
                return (T) getter.invoke(instance, new Object[]{});
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException(ex);
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        }

        @Override
        public int compareTo(IssueNode<MantisIssue>.IssueProperty<T> o) {
            try {
                T value = getValue();
                if(o == null)  {
                    return 1;
                }
                T remoteValue = (T) o.getValue();
                if(value instanceof Comparable) {
                    return ((Comparable)value).compareTo(remoteValue);
                } else {
                    return toString().compareTo(o.toString());
                }
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
            
        }
    }
}
