package com.gilesc
package mynab
package testkit

import com.gilesc.mynab.testkit.generator.DataGenerators

import org.scalatest.FlatSpec
import org.scalatest.Matchers

abstract class TestCase
  extends FlatSpec
    with Matchers
    with DataGenerators

