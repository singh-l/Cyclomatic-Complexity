package com.accenture.cc.servic;

import java.util.Comparator;
import com.accenture.cc.domain.ClassBO;

public class SortingHelperStringComp implements Comparator<ClassBO>
{

  @Override
  public int compare(ClassBO o1, ClassBO o2)
  {
    // TODO Auto-generated method stub
    return o1.getClassName().compareToIgnoreCase(o2.getClassName());
  }

}
