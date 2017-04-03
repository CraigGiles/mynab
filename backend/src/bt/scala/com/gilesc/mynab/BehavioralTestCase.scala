package com.gilesc
package mynab

import org.scalatest.fixture.FlatSpec
import org.scalatest.Matchers
import org.scalatest.BeforeAndAfterAll

import scalikejdbc._
import scalikejdbc.scalatest.AutoRollback

import com.gilesc.mynab.persistence.DatabaseInitializer

abstract class BehavioralTestCase extends FlatSpec 
  with Matchers
  with TestCaseHelpers
  with AutoRollback
  with BeforeAndAfterAll {

  override def beforeAll() = {
    DatabaseInitializer.init()
    MockDatabase.migrate()
  }

  override def afterAll() = {
    DatabaseInitializer.stop()
  }
}
