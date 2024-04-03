package beans;

import java.io.Serializable;

import javax.persistence.*;

@Entity
public class Attendee implements Serializable, Comparable
{
    private String name;
    private String title;
    private String company;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getCompany()
    {
        return company;
    }

    public void setCompany(String company)
    {
        this.company = company;
    }

    public Long getId()
    {
        return id;
    }


    public Attendee()
    {
    }

    public Attendee(String name, String title, String company)
    {
        this.name = name;
        this.title = title;
        this.company = company;
    }

    @Override
    public int compareTo(Object o)
    {
        if (!(o instanceof Attendee))
            return 0;
        else
        {
            Attendee a = (Attendee)o;
            return this.name.compareTo(a.getName());
        
        }
    }
}
