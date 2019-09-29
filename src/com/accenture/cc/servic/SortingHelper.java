package com.accenture.cc.servic;

import java.util.Comparator;
import com.accenture.cc.domain.ClassBO;

public class SortingHelper implements Comparator<ClassBO>
{

  @Override
  public int compare(ClassBO o1, ClassBO o2)
  {
    // TODO Auto-generated method stub
    return o2.getCcn() - o1.getCcn();
  }

}
