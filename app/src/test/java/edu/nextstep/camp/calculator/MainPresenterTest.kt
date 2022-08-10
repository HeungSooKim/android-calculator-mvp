package edu.nextstep.camp.calculator

import com.google.common.truth.Truth.assertThat
import edu.nextstep.camp.calculator.domain.Expression
import edu.nextstep.camp.calculator.domain.Operator
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class MainPresenterTest {

    private lateinit var presenter: MainPresenter
    private lateinit var view: MainContract.View

    @Before
    fun setUp() {
        view = mockk()
        presenter = MainPresenter(view)
    }

    @Test
    fun `숫자가 입력되면 화면에 보여준다`() {
        // Given
        val expression = Expression(listOf(3))
        every { view.showExpression(expression.toString()) } answers { nothing }

        // When
        presenter.appendOperand(3)

        // Then
        verify { view.showExpression(expression.toString()) }

    }

    @Test
    fun `연산자가 입력되면 화면에 보여준다`() {
        // Given
        val expression = slot<String>()
        every { view.showExpression(capture(expression)) } answers { nothing }

        // When
        presenter.appendOperand(3)
        presenter.appendOperator(Operator.Plus)

        // Then
        val actual = expression.captured
        assertThat(actual).isEqualTo("3 +")
        verify { view.showExpression(actual) }
    }

    @Test
    fun `삭제를 하면 마지막 문자를 지우고 화면에 보여준다`() {
        // Given
        val expression = slot<String>()
        every { view.showExpression(capture(expression)) } answers { nothing }

        // When
        presenter.appendOperand(3)
        presenter.appendOperator(Operator.Plus)
        presenter.deleteLast()

        // Then
        val actual = expression.captured
        assertThat(actual).isEqualTo("3")
        verify { view.showExpression(actual) }
    }

    @Test
    fun `계산을 하면 연산된 결과를 화면에 보여준다`() {
        // Given
        val expression = slot<String>()
        every { view.showExpression(capture(expression)) } answers { nothing }

        // When
        presenter.appendOperand(3)
        presenter.appendOperator(Operator.Plus)
        presenter.appendOperand(3)
        presenter.calculate()

        // Then
        val actual = expression.captured
        assertThat(actual).isEqualTo("6")
        verify { view.showExpression(actual) }
    }

    @Test
    fun `완성되지 않은 표현식으로 계산을 실행하면 에러 토스트를 보여준다`() {
        // Given
        val expression = slot<String>()
        every { view.showExpression(capture(expression)) } answers { nothing }
        every { view.showErrorToast() } answers { nothing }

        // When
        presenter.appendOperand(3)
        presenter.appendOperator(Operator.Plus)
        presenter.calculate()

        // Then
        verify { view.showErrorToast() }
    }
}