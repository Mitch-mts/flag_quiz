package com.example.flagquiz.view

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.flagquiz.R
import com.example.flagquiz.database.FlagsDao
import com.example.flagquiz.databinding.FragmentQuizBinding
import com.example.flagquiz.model.FlagsModel
import com.techmania.flagquizwithsqlitedemo.DatabaseCopyHelper

class QuizFragment : Fragment() {
    lateinit var quizFragmentBinding: FragmentQuizBinding
    var flagList = ArrayList<FlagsModel>()

    var correctNumber = 0
    var wrongNumber = 0
    var emptyNumber = 0
    var questionNumber = 0

    lateinit var correctFlag: FlagsModel
    var wrongFlagList = ArrayList<FlagsModel>()
    var dao: FlagsDao = FlagsDao()
    var optionControl = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        quizFragmentBinding = FragmentQuizBinding.inflate(inflater, container, false)

        val dao = FlagsDao()
        flagList = dao.getRandomTenRecords(DatabaseCopyHelper(requireActivity()))

        for (flag in flagList) {
            Log.d("flags", flag.id.toString())
            Log.d("flags", flag.countryName)
            Log.d("flags", flag.flagName)
            Log.d("flags", "-------------------------")
        }

        showData()


        quizFragmentBinding.btnA.setOnClickListener {
            answerControl(quizFragmentBinding.btnA)
        }

        quizFragmentBinding.btnB.setOnClickListener {
            answerControl(quizFragmentBinding.btnB)
        }

        quizFragmentBinding.btnC.setOnClickListener {
            answerControl(quizFragmentBinding.btnC)
        }

        quizFragmentBinding.btnD.setOnClickListener {
            answerControl(quizFragmentBinding.btnD)
        }

        quizFragmentBinding.btnNext.setOnClickListener {
            questionNumber++

            if(questionNumber > 5) {
                if(!optionControl) {
                    emptyNumber++
                }

                //Toast.makeText(requireActivity(), "The quiz is finished.", Toast.LENGTH_SHORT).show()

                /**
                 * Using Bundle to navigate to the next fragment with safe args.
                 ***/
                val bundle = Bundle().apply {
                    putInt("correct", correctNumber)
                    putInt("wrong", wrongNumber)
                    putInt("empty", emptyNumber)
                }

                this.findNavController().navigate(R.id.action_quizFragment_to_resultFragment,
                    bundle,
                    NavOptions.Builder().setPopUpTo(R.id.homeFragment, false).build())


                /**
                 * Using Direction to navigate to the next fragment with safe args.You can use the one above or the one below. Only one of them works
                 ***/
                /*val direction = QuizFragmentDirections.actionQuizFragmentToResultFragment(correctNumber, wrongNumber, emptyNumber)
                this.findNavController().navigate(R.id.action_quizFragment_to_resultFragment,
                    direction.arguments,
                    NavOptions.Builder().setPopUpTo(R.id.homeFragment, false).build())*/


            } else {
                showData()

                if(!optionControl) {
                    emptyNumber++
                    quizFragmentBinding.emptyNumber.text = emptyNumber.toString()

                } else {
                    resetButtonProperties()
                }
            }

            optionControl = false

        }


        return quizFragmentBinding.root
    }

    fun showData() {
        quizFragmentBinding.questionText.text = resources.getString(R.string.question_number).plus(" ").plus(questionNumber + 1)

        correctFlag = flagList[questionNumber]
        quizFragmentBinding.imagaeViewFlag.setImageResource(resources.getIdentifier(correctFlag.flagName, "drawable", requireActivity().packageName))

        wrongFlagList = dao.getRandomThreeRecords(DatabaseCopyHelper(requireActivity()), correctFlag.id)

        val mixOptions = HashSet<FlagsModel>()
        mixOptions.clear()

        mixOptions.add(correctFlag)
        mixOptions.add(wrongFlagList[0])
        mixOptions.add(wrongFlagList[1])
        mixOptions.add(wrongFlagList[2])

        val options = ArrayList<FlagsModel>()
        options.clear()

        for (eachFlag in mixOptions) {
            options.add(eachFlag)
        }

        quizFragmentBinding.btnA.text = options[0].countryName
        quizFragmentBinding.btnB.text = options[1].countryName
        quizFragmentBinding.btnC.text = options[2].countryName
        quizFragmentBinding.btnD.text = options[3].countryName

    }


    fun answerControl(button: Button) {
        val clickedOption = button.text.toString()
        val correctAnswer = correctFlag.countryName

        if (clickedOption == correctAnswer) {
            correctNumber++
            quizFragmentBinding.correctNumber.text = correctNumber.toString()
            button.setBackgroundColor(Color.GREEN)

        } else {
            wrongNumber++
            quizFragmentBinding.wrongNumber.text = wrongNumber.toString()
            button.setBackgroundColor(Color.RED)
            button.setTextColor(Color.WHITE)

            when(correctAnswer) {
                quizFragmentBinding.btnA.text -> quizFragmentBinding.btnA.setBackgroundColor(Color.GREEN)
                quizFragmentBinding.btnB.text -> quizFragmentBinding.btnB.setBackgroundColor(Color.GREEN)
                quizFragmentBinding.btnC.text -> quizFragmentBinding.btnC.setBackgroundColor(Color.GREEN)
                quizFragmentBinding.btnD.text -> quizFragmentBinding.btnD.setBackgroundColor(Color.GREEN)
            }

        }

        quizFragmentBinding.btnA.isClickable = false
        quizFragmentBinding.btnB.isClickable = false
        quizFragmentBinding.btnC.isClickable = false
        quizFragmentBinding.btnD.isClickable = false

        optionControl = true

    }

    fun resetButtonProperties() {
        quizFragmentBinding.btnA.apply {
            setBackgroundColor(Color.WHITE)
            setTextColor(resources.getColor(R.color.purple, requireActivity().theme))
            isClickable =  true
        }

        quizFragmentBinding.btnB.apply {
            setBackgroundColor(Color.WHITE)
            setTextColor(resources.getColor(R.color.purple, requireActivity().theme))
            isClickable =  true
        }

        quizFragmentBinding.btnC.apply {
            setBackgroundColor(Color.WHITE)
            setTextColor(resources.getColor(R.color.purple, requireActivity().theme))
            isClickable =  true
        }

        quizFragmentBinding.btnD.apply {
            setBackgroundColor(Color.WHITE)
            setTextColor(resources.getColor(R.color.purple, requireActivity().theme))
            isClickable =  true
        }

    }

}