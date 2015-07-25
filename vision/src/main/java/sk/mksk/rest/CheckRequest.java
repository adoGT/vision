package sk.mksk.rest;

import java.lang.Override;

public class CheckRequest
{

   private String name;

   private String description;

   public CheckRequest()
   {
      // Empty constructor for JAXB
   }
   
   public CheckRequest(String name, String description){
	   this.name = name;
	   this.description = description;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public String getDescription()
   {
      return description;
   }

   public void setDescription(String description)
   {
      this.description = description;
   }

   @Override
   public String toString()
   {
      String result = getClass().getSimpleName() + " ";
      if (name != null && !name.trim().isEmpty())
         result += "name: " + name;
      if (description != null && !description.trim().isEmpty())
         result += ", description: " + description;
      return result;
   }
}
