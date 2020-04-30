package tomeksz.batch.avro

import com.holdenkarau.spark.testing.DataFrameSuiteBase
import org.apache.avro.SchemaBuilder
import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.functions._
import tomeksz.IntegrationSpec

class AvroSchemaEvolutionDemonstrationTest extends IntegrationSpec with DataFrameSuiteBase {

  it should "read and write same schema version file" in {
    import spark.implicits._

    Given("sample data to safe")
    val employees = Seq(
      Employee("James", "Smith", 2002, "M", 34021),
      Employee("Michael", "Rose", 2004, "M", 65322),
      Employee("Mary", "Brown", 2008, "F", 124322)
    ).toDF()

    When("AVRO file written")
    employees
      .repartition(col("hireYear"))
      .write
      .mode(SaveMode.Overwrite)
      .partitionBy("hireYear")
      .format("avro")
      .save("tmp/employees.avro")

    And("Read the same file")
    val readFromDisk = spark
      .read
      .format("avro")
      .load("tmp/employees.avro")

    Then("")
    val sortOrder = Seq(col("firstName"), col("lastName"))
    assertDataFrameDataEquals(employees.sort(sortOrder: _*), readFromDisk.sort(sortOrder: _*))
  }

  ignore should "adding new field in new version" in {
    import spark.implicits._

    Given("sample data to safe in old format")
    val employees = Seq(
      Employee("James", "Smith", 2002, "M", 34021),
      Employee("Michael", "Rose", 2004, "M", 65322),
      Employee("Mary", "Brown", 2008, "F", 124322)
    ).toDF()

    val employeeOldSchema = SchemaBuilder.builder()
      .record("Employee").fields()
      .requiredString("firstName")
      .requiredString("lastName")
      .requiredInt("hireYear")
      .requiredString("gender")
      .requiredDouble("salary")
        .endRecord()

    //    val employeeOldSchema = StructType(
    //      StructField("firstName", DataTypes.StringType, nullable = false)::
    //      StructField("lastName", DataTypes.StringType, nullable = false)::
    //      StructField("hireYear", DataTypes.IntegerType, nullable = false)::
    //      StructField("gender", DataTypes.StringType, nullable = false)::
    //      StructField("salary", DataTypes.DoubleType)::
    //        Nil)

    When("AVRO file written in old format")
    employees
      .repartition(col("hireYear"))
      .write
      .mode(SaveMode.Overwrite)
      .option("avroSchema", employeeOldSchema.toString)
//      .partitionBy("hireYear")
      .format("avro")
      .save("tmp/employees.avro")

    And("Read the same file into the new format. See isActive added as the last field")
//    val employeeNewSchema = StructType(
//      StructField("firstName", DataTypes.StringType, nullable = false) ::
//        StructField("lastName", DataTypes.StringType, nullable = false) ::
//        StructField("hireYear", DataTypes.IntegerType, nullable = false) ::
//        StructField("gender", DataTypes.StringType, nullable = false) ::
//        StructField("salary", DataTypes.DoubleType) ::
//        StructField("isActive", DataTypes.BooleanType, nullable = false) ::
//        Nil)

    val employeeNewSchema = SchemaBuilder.builder()
      .record("Employee").fields()
      .name("firstName").`type`().stringType().noDefault()
      .name("lastName").`type`().stringType().noDefault()
      .name("hireYear").`type`().intType().noDefault()
      .name("gender").`type`().stringType().noDefault()
      .name("salary").`type`().stringType().noDefault()
      .name("disabled").`type`().booleanType().booleanDefault(false)
      .endRecord()

    val readFromDisk = spark
      .read
      .option("avroSchema", employeeNewSchema.toString)
      .format("avro")
      .load("tmp/employees.avro")

    Then("")
    val sortOrder = Seq(col("firstName"), col("lastName"))
    assertDataFrameDataEquals(employees.sort(sortOrder: _*), readFromDisk.sort(sortOrder: _*))
  }

}
