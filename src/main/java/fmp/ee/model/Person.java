package fmp.ee.model;

import fmp.ee.WeekendList;

/**
 * Created by pedrozaf on 6/5/15.
 */
public class Person
{
  public final String name, address, email, phone, religion;
  public final Integer age;

  public Person(String name, String address, String email, String phone, Integer age, String religion)
  {
    this.name = name;
    this.address = address;
    this.email = email;
    this.phone = phone;
    this.religion = religion;
    this.age = age;
  }

  @Override
  public String toString()
  {
    StringBuilder buf = new StringBuilder();
    WeekendList.append(name, buf);
    WeekendList.append(address, buf);
    WeekendList.append(phone, buf);
    WeekendList.append(age, buf);
    WeekendList.append(religion, buf);
    // ignore email
    return WeekendList.trimmed(buf);
  }
}
