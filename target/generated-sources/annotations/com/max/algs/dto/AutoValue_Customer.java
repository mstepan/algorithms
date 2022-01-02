
package com.max.algs.dto;

import javax.annotation.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_Customer extends Customer {

  private final String name;
  private final int age;
  private final String ssn;

  private AutoValue_Customer(
      String name,
      int age,
      String ssn) {
    this.name = name;
    this.age = age;
    this.ssn = ssn;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public int getAge() {
    return age;
  }

  @Override
  public String getSsn() {
    return ssn;
  }

  @Override
  public String toString() {
    return "Customer{"
        + "name=" + name + ", "
        + "age=" + age + ", "
        + "ssn=" + ssn
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Customer) {
      Customer that = (Customer) o;
      return (this.name.equals(that.getName()))
           && (this.age == that.getAge())
           && (this.ssn.equals(that.getSsn()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= this.name.hashCode();
    h *= 1000003;
    h ^= this.age;
    h *= 1000003;
    h ^= this.ssn.hashCode();
    return h;
  }

  static final class Builder extends Customer.Builder {
    private String name;
    private Integer age;
    private String ssn;
    Builder() {
    }
    @Override
    public Customer.Builder name(String name) {
      if (name == null) {
        throw new NullPointerException("Null name");
      }
      this.name = name;
      return this;
    }
    @Override
    public Customer.Builder age(int age) {
      this.age = age;
      return this;
    }
    @Override
    public Customer.Builder ssn(String ssn) {
      if (ssn == null) {
        throw new NullPointerException("Null ssn");
      }
      this.ssn = ssn;
      return this;
    }
    @Override
    public Customer build() {
      String missing = "";
      if (this.name == null) {
        missing += " name";
      }
      if (this.age == null) {
        missing += " age";
      }
      if (this.ssn == null) {
        missing += " ssn";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_Customer(
          this.name,
          this.age,
          this.ssn);
    }
  }

}
