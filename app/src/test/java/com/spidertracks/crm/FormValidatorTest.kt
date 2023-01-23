package com.spidertracks.crm
import com.spidertracks.crm.databinding.FragmentCustomerBinding
import com.spidertracks.crm.ui.fragment.FormValidator
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class FormValidatorTest {

    private val formValidator = FormValidator()
    @Mock
    private lateinit var binding: FragmentCustomerBinding

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `validate form with valid data`() {
        // Arrange
        Mockito.`when`(binding.etName.text.toString()).thenReturn("John Smith")
        Mockito.`when`(binding.etJoinDate.text.toString()).thenReturn("2022-01-01")
        Mockito.`when`(binding.etEmail.text.toString()).thenReturn("john.smith@example.com")
        Mockito.`when`(binding.etPhoneNo.text.toString()).thenReturn("+1234567890")

        // Act
        val result = formValidator.validate(binding)

        // Assert
        Assert.assertTrue(result)
    }

    @Test
    fun `validate form with invalid data`() {
        // Arrange
        Mockito.`when`(binding.etName.text.toString()).thenReturn("")
        Mockito.`when`(binding.etJoinDate.text.toString()).thenReturn("2022-01-01")
        Mockito.`when`(binding.etEmail.text.toString()).thenReturn("john.smith")
        Mockito.`when`(binding.etPhoneNo.text.toString()).thenReturn("+1234567890")

        // Act
        val result = formValidator.validate(binding)

        // Assert
        Assert.assertTrue(result)
    }
}
