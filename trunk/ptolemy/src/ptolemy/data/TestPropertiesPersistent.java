/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ptolemy.data;

/**
 *
 * @author q
 */
public class TestPropertiesPersistent {
	private static class TestBean extends PropertiesPersistent {
		private String name;
		private String surname;
		private Integer age;
		private Double iq;

		@Getter(property="person.age")
		public Integer getAge() {
			return age;
		}

		@Setter(property="person.age")
		public void setAge(Integer age) {
			this.age = age;
		}

		@Getter(property="person.iq")
		public Double getIq() {
			return iq;
		}

		@Setter(property="person.iq")
		public void setIq(Double iq) {
			this.iq = iq;
		}

		@Getter(property="person.name")
		public String getName() {
			return name;
		}

		@Setter(property="person.name")
		public void setName(String name) {
			this.name = name;
		}

		@Getter(property="person.surname")
		public String getSurname() {
			return surname;
		}

		@Setter(property="person.surname")
		public void setSurname(String surname) {
			this.surname = surname;
		}

		public String toString() {
			return getName() + ", " + getSurname() + " (age: " +
				getAge() + ", IQ: " + getIq() + ")";
		}
	}

	public static void main(String[] args) throws Exception {
		TestBean b = new TestBean();
		b.setName("Justin");
		b.setSurname("Case");
		b.setAge(27);
		b.setIq(187.995);
		b.saveToProperties(b, TestBean.class, "b.properties");
		/*
		 * contents of b.properties:
		 * #Sat Mar 12 17:45:39 CET 2011
		 * person.iq=187.995
		 * person.name=Justin
		 * person.age=27
		 * person.surname=Case
		 */

		TestBean b1 = new TestBean();
		b1.loadFromProperties(b1, TestBean.class, "b.properties");
		System.out.println("BEAN: " + b1.toString());
		/*
		 * prints:
		 * BEAN: Justin, Case (age: 27, IQ: 187.995)
		 */
	}
}
