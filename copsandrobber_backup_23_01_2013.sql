
-- phpMyAdmin SQL Dump
-- version 2.11.4
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jan 23, 2013 at 10:05 AM
-- Server version: 5.1.57
-- PHP Version: 5.2.17

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

--
-- Database: `a2417272_uhvati`
--

-- --------------------------------------------------------

--
-- Table structure for table `igraci`
--

CREATE TABLE `igraci` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `idIgraca` varchar(200) NOT NULL,
  `idIgre` int(10) NOT NULL,
  `uloga` varchar(1) NOT NULL,
  `latitude` varchar(30) NOT NULL,
  `longitude` varchar(30) NOT NULL,
  `na_poziciji` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=350 ;

--
-- Dumping data for table `igraci`
--

INSERT INTO `igraci` VALUES(349, 'APA91bHCeO7d4p4i_dh6t3gmkkNkxpKRrumYuQvr5qkTpN112e-quZ5tWkp6dsIN_VrbFsN4bcfHEvE0VaYxzoxE3ksGaYbzF9ShzNnY6RGibYtcV1CJxRr3usDPmdqcGpHvPJaRKIMXH2g4Q6MbJRcKiRQFLFC-fw', 162, 'L', '0.0', '0.0', 0);
INSERT INTO `igraci` VALUES(347, 'APA91bF82uJq-6c2YAqUfQY7qIcHxZaRJQvX0rB8efLTCigGAo_JE8jxjTPCDb8fYrNKleksbgTmIqJ7TmYaPjvrZe-xnHDGhcPVZQu6Zqb_iIBarmzcxkQ75nl7or8TOk63uCIWsxsbjeQAD4M85WEOclOenafsPg', 161, 'P', '43.315008', '21.888378', 1);
INSERT INTO `igraci` VALUES(348, 'APA91bFO4GhR4i5lGF110NSzXsJUjUREuz-z66pB7MIvFuFHSKq8eUuh4PghU0jBC5qZlJ3js3FPVX1UUntDSnwuXp2ZyfMORQdqPGuULkChuYXKk5WMA0XF37EyGgENwsqGywOXeCXuIlECTnH7XNjteuNTdLrRkA', 161, 'L', '0.0', '0.0', 0);
INSERT INTO `igraci` VALUES(346, 'APA91bFO4GhR4i5lGF110NSzXsJUjUREuz-z66pB7MIvFuFHSKq8eUuh4PghU0jBC5qZlJ3js3FPVX1UUntDSnwuXp2ZyfMORQdqPGuULkChuYXKk5WMA0XF37EyGgENwsqGywOXeCXuIlECTnH7XNjteuNTdLrRkA', 161, 'P', '43.31875166666667', '21.90308333333333', 1);
INSERT INTO `igraci` VALUES(345, 'APA91bF-2FcSY11OGFgKT4plHkcVCK_pzONSzeLV9ulb3Fwmw4OetfOAAnT6ZG8OdRh_Xg-sLazrcTsa7zwDEhzgQedtMPrC3-VcUXkXT5UoZruQudzUCWMJ0StocDrLg8nwJ7lrXbSpEQvvt-0zYFSlhweMMRGzJw', 161, 'P', '43.31875166666667', '21.90308333333333', 1);
INSERT INTO `igraci` VALUES(343, 'APA91bExK221oLAUTvd0k3Fuz7A_gqLfpBdn5DJfmZvFXa3DkpWDkDDJv6ZMtz3lRtEbf7qAn4-cY6vg4kBodWkEIk5fsGlUzKA-1aXgnCUEKjMGn99CygMOa-p1vWzJxcAtQkK84NT5lPtaAuv-amRs4zvYDmWBOQ', 160, 'P', '43.32604', '21.894256', 1);
INSERT INTO `igraci` VALUES(342, 'APA91bGbURgUjDMonSxIr1fdua92kuRdbbOhYcIf27iV8Pgt46QZge8h2korsII29wqnSDI4TPcWtQSfYeB9Jk-W9NB9DwlUxyzaTVtTAbM_-RVB1oWR0eeKekcatCE5FaxAI2m61DG6I-e1FSt3E6WZZUAHIZnpJQ', 160, 'P', '43.316555', '21.90308333333333', 1);
INSERT INTO `igraci` VALUES(341, 'APA91bG5jxcqC_O82T2bFzijXK0RPQ31g7B_NVYpo9yY-VQAp02Tb4qvwxQMcrRRO0Aiz01ZoqW1UpdkHLgg8n5kA9GNYl4X819zZc5sm4aPtL7ZMokSCj54m8GXPJVWxtOjZ17TzL10iV6KVY9aj0GHnpFG3ml1-Q', 160, 'P', '43.38788833333333', '21.88848666666667', 1);
INSERT INTO `igraci` VALUES(340, 'APA91bFhYITk6BedJdpNfdPecN9ywmgnLESpSagoCokubIMvXXLzv7CdOgzhceG5yLuwFXlfN8UhSse-2Z-YkrN2tnufD8vAJuTSL1Okeplqd6ArzXY8PdPFXgdXyqpsRjXqly7uOqoWXtAcpDImXxyn-tqnE-XMYg', 160, 'L', '43.31452833333333', '21.88848666666667', 1);
INSERT INTO `igraci` VALUES(344, 'APA91bF1tdfngQeB66BLKbL75JeSJYj0p5r79nnjTo1iJqd2q2kS0NDW1j-z02apl5Lvg415pZXJ83tSjsTADW-CiouH5yUNYr5W9yGMCwzmZm7Z3oT4HftpGSDGEW1VUDSWV090_TXCbx-KWIyE1I95KsVqCZT2VQ', 161, 'L', '43.31452833333333', '21.88848666666667', 1);
INSERT INTO `igraci` VALUES(339, 'APA91bEUvRStPPPl17I6emrMYGqaauqG7e30gWkNPjMowmY-yItNbLAKxMWB6O0LjJFVJEYu6ttc3AXzE9aUGKB91XOh7GtenLsvxRuWKjhiYPTes932eCZpS-MWRJC2-A9h-sbPLcza2C_TpPXMrxQ9DsHwHGijVQ', 159, 'P', '43.316555', '21.90308333333333', 1);
INSERT INTO `igraci` VALUES(338, 'APA91bEuAkxEuqnz_Ck65jW2VcegMSYEyrhpnaJPZ0pSGxe65q3wMlGPCFApJdw_jV15t4UxVxPQMuL5oRzY4-LdGjBr8D6KikigNyHj5QIASW-ZXXMaxGZVZHTf99bs4Pqkzlun8xnddcLAJSwLXE87hGYqP7Rf1A', 159, 'P', '43.38788833333333', '21.88848666666667', 1);
INSERT INTO `igraci` VALUES(337, 'APA91bExK221oLAUTvd0k3Fuz7A_gqLfpBdn5DJfmZvFXa3DkpWDkDDJv6ZMtz3lRtEbf7qAn4-cY6vg4kBodWkEIk5fsGlUzKA-1aXgnCUEKjMGn99CygMOa-p1vWzJxcAtQkK84NT5lPtaAuv-amRs4zvYDmWBOQ', 159, 'P', '43.32604', '21.894256', 1);
INSERT INTO `igraci` VALUES(336, 'APA91bFshunzoMCNYRPn3n3RxPh9PTFVmKpvNr4hG8K8O9wYn8PjywdTz5Ys-vb9AsmSdEYpkUFvmmYyBZba0XoySnVjRjgUQ-L-cOPeycGdJajKP4Polb8aUTdODwD1pEw2d1PBLFbTtVqogHI6NboFKwdHD-EvCQ', 159, 'L', '43.31452833333333', '21.88848666666667', 1);
INSERT INTO `igraci` VALUES(335, 'APA91bF6QA8tQNHcZkxzFekqCVvcjdCWxP3uVVqj1Ra8-LVwOpIr6mVXWi2sPaTdDCgupwmvvrq7qvpzD4Le6uurweYwAuiV4WmBYicc9Js-XDvREImtH5Y57H93gFDss29Chm0H5ILUnwyuNHfz_sooCmM_-ZlO9Q', 156, 'P', '0.0', '0.0', 0);
INSERT INTO `igraci` VALUES(334, 'APA91bGj7GT129j9FXR__788-w6AOqz6ND9HqG7aqyIrsG1_8fARwdRcuc263y4rhaXDZDRweXEiphyjFafaQbRlVyMagGVBPlF1tbthJXoSbuKTn8NTPt-LNCkZhd9Il4j8wJtM3xfWpSfbO80_vtxDxJqeE6OfFA', 156, 'P', '0.0', '0.0', 0);
INSERT INTO `igraci` VALUES(333, 'APA91bG1Y2anl4gLMzmyZJJC7KedMwuxfAehbOn5Y835wTUBVVpUlOkhT1jLLkbHSegxBiUs_GQxC7QbvSFfJ3H0oykhL4fBBzvKvlpaiM44-60EJ071VPsT2bMgSEQ6CyH4_fAaukYhEcknv1I4MUOoojUexsxoGg', 153, 'P', '43.31225500000001', '21.89798', 0);
INSERT INTO `igraci` VALUES(332, 'APA91bGbtFvj1lsGs-CtRauXPSWobtmmWjjcutY9rw4L9QUZdKSkb3a6nx3bGGuzVMbengV-CGwB3r13fSN0k2GB8-ohKe-Hghc1PuRu1N1O1xxk_ekhv-A4jE7NCGJoG3Wjn5U7d5mCbXHKq469kVlMNIbqJxEYvw', 153, 'P', '43.23554666666667', '21.89798', 0);
INSERT INTO `igraci` VALUES(331, 'APA91bG1Y2anl4gLMzmyZJJC7KedMwuxfAehbOn5Y835wTUBVVpUlOkhT1jLLkbHSegxBiUs_GQxC7QbvSFfJ3H0oykhL4fBBzvKvlpaiM44-60EJ071VPsT2bMgSEQ6CyH4_fAaukYhEcknv1I4MUOoojUexsxoGg', 154, 'P', '0.0', '0.0', 0);
INSERT INTO `igraci` VALUES(330, 'APA91bGbtFvj1lsGs-CtRauXPSWobtmmWjjcutY9rw4L9QUZdKSkb3a6nx3bGGuzVMbengV-CGwB3r13fSN0k2GB8-ohKe-Hghc1PuRu1N1O1xxk_ekhv-A4jE7NCGJoG3Wjn5U7d5mCbXHKq469kVlMNIbqJxEYvw', 154, 'P', '0.0', '0.0', 0);
INSERT INTO `igraci` VALUES(329, 'APA91bEoAgoFLMirsf_hrRtNII4XGO3-jEaI3tAAkj13_9J-7r0WmIdNQFrbFdwkBMZ8e7TvMGbMUZ0JVz7nOWK3CJqIbDzko7tDXgFM5dMMY2NCBUcEVIGbNkLgfeg0zirfgODVB1-jTtgLYzO7UkqIuuyhf6r-Rg', 157, 'P', '43.14658666666666', '21.89798', 0);
INSERT INTO `igraci` VALUES(328, 'APA91bG9ewlEaJQzSNkOc310z3XnlPyzEFtWus_SnC7BDUSy8vuQXXtEYWmrpcwTvqY2AdyrcXXarKCY7zgxr-y2ldOenaogRotHkeU-mYGNEo8dIMykiLMFDtLGOFUsM8mrk2qpbESyK5iT-su6AFZe-RLzQe_6NQ', 157, 'P', '43.18789666666667', '21.89798', 0);
INSERT INTO `igraci` VALUES(327, 'APA91bE_98VIiLqCbmdfyIrDp9ClDZbJ6M15zc-yHAUnlFNsASvD4YHTBGHkACkbCuO7ReSuGd4lBd0qmjJAFVetwA_Llte3X8_uwsXOx9bqe8nfq8bdLlvsqq6YuVw_19aEMhKBFnIMZwIiRyW7WOSTRe4d87MpPg', 158, 'P', '43.18789666666667', '21.89798', 0);
INSERT INTO `igraci` VALUES(326, 'APA91bEVCDvFjFQDZJd5xSdPqLTnDt4ogubgNeCPUF6VE_H1x4bRoFHATuyioYKhUbmyVRfOz4bB44H82dqMYTXx_k4rATWyzLRz8Ljz8tx09JfoQBwRckQl73WT7VG5GmzmpPumtbB9TOdVvBsxM8Wio_FdcZoL7Q', 158, 'P', '43.179899999999996', '21.89798', 0);
INSERT INTO `igraci` VALUES(325, 'APA91bGvlcBS0pgNiSUmnQwOUOQVbDqhRgStb3d8XsCUXGV_FPwDlQ80ntaiTWkZK-fkVB5pWUjo_MbYvH9H_VJO7MsUWNApAcgN4OzQpQtieTK6dmO9N9mGSQhZiBaex9uFlv5CLrQ3wtCK_ajF0P6kTNVb1bDqbA', 158, 'P', '43.187999999999995', '21.898778333333333', 0);
INSERT INTO `igraci` VALUES(324, 'APA91bEVCDvFjFQDZJd5xSdPqLTnDt4ogubgNeCPUF6VE_H1x4bRoFHATuyioYKhUbmyVRfOz4bB44H82dqMYTXx_k4rATWyzLRz8Ljz8tx09JfoQBwRckQl73WT7VG5GmzmpPumtbB9TOdVvBsxM8Wio_FdcZoL7Q', 158, 'L', '43.179899999999996', '21.89798', 0);
INSERT INTO `igraci` VALUES(323, 'APA91bGc5hgYzKEfq_6xT91ogGtTd53pSoION-qzjso6f-oMc77n4OpFxtLG9mlm7L1dpQBw4T23_36Tb5M1Gd6MntnTOnZDz8Ep8mSfUxB9PUMoeWkMYnWz8tpVyW52Ii-Fk233tv-H36LdHAGHkfTc0i_NePphTw', 157, 'P', '0.0', '0.0', 0);
INSERT INTO `igraci` VALUES(322, 'APA91bG5Ruk-sFr4RZHD_R-zzhLQlrKoIoClT_c8AMorlW0pQXOjQKon8-Ogk4ea1ngZseW7RmVmRUO3tuyu5M84OzY59kx9ehMYCI0LKBT3CP9wg40v4SFab7i9Z8c3LQGVmu4s3KU0J1OHZObWGwPpTf30rZXbMw', 157, 'L', '43.318615', '21.89267166666667', 0);
INSERT INTO `igraci` VALUES(321, 'APA91bGvlcBS0pgNiSUmnQwOUOQVbDqhRgStb3d8XsCUXGV_FPwDlQ80ntaiTWkZK-fkVB5pWUjo_MbYvH9H_VJO7MsUWNApAcgN4OzQpQtieTK6dmO9N9mGSQhZiBaex9uFlv5CLrQ3wtCK_ajF0P6kTNVb1bDqbA', 156, 'P', '43.132588333333324', '21.889986666666665', 0);
INSERT INTO `igraci` VALUES(294, 'APA91bGAM8uPouxfyM3ULrm9xufzxctAEvOEAGpMttTIOUX_ccb8F9DeO7Zv3Kk646OmsXLNNqrdk2xPEOkc0IECLz1JdSveuEXuMIGO4w48ZkeXaOWPvTnvyYoD6wxuZ7sL-KVrjlmvYZEWTiuBVXi2aIgA3jjE0w', 143, 'P', '0.0', '0.0', 0);
INSERT INTO `igraci` VALUES(295, 'APA91bH1D6D3g4hyaVUp0XDiW90rctuALMvQzT_Jx-QhUsQqvOR3aAeP6e4EDGGv6k8pkcRpmY8LJsPkCxM-HkdMHSy2HudWFjzLrl101X6kR_IWiXBkBxFRBtN1Ave-rtpBbf0jN31g1x0d2kdRf0Rq1yY9aLJu3g', 143, 'P', '0.0', '0.0', 0);
INSERT INTO `igraci` VALUES(296, 'APA91bGIeOGDu32qmK8pIn8ifv-x3bnfzsHnARHOnUSwWOCpezAEevPrinYiDd40Hir8YVosxwrUqkJCh0BOhny_F6LgLMtxJKdQjnaHI7jnyskXvrQ-RnKnZajHnZf_y4vcU9ep8m6zHqSUuF9Mdzd3AC4G0jPyag', 144, 'L', '43.31693166666667', '21.894795', 0);
INSERT INTO `igraci` VALUES(297, 'APA91bFt3WoISl7a-SIPvs_OoG6jzRDvN9s1EhUlMQvQPgCJoO0iFgr3dD-FtbUiw-VB9DTYXbeaH0lpHEBVT3y9Cd0f0GtRMA3yAMsQZLgpjD0jolVcEEx21yYclqvWN9m0WlW0gBaCltSMtllWHqiMvkqgs86htw', 144, 'P', '0.0', '0.0', 0);
INSERT INTO `igraci` VALUES(298, 'APA91bEB5BA5kgfEZ3RTG7U8BLfCvcfrf10le2-NfzPv7puQyoNXCS9nEHErKeoI1C1jaiJQDtLBqV8JcIbTcZN0OQw3DNvWK1-cWn48txBpGjy3SrcWSBtiUn2s4f5GrMfge0Fqwpsobtcr1cqqoVi1zIVuv7CIqg', 145, 'L', '43.34487833333333', '21.889799999999997', 0);
INSERT INTO `igraci` VALUES(299, 'APA91bFkLB0GNuWM12sFgpEaAKwo4PHqdtR4phn9ElS4uy5btfkCxVEnWmGSKyI6Bx969ZliqXkmoLHyNwko08VR4RVZWxxQPCpOUbOLLcd_y0aaRESt4r_aYg7fHa5LelY3y0fFQeNOSKhyXrkBevfsKHALPVMc0Q', 145, 'P', '43.344455', '21.889799999999997', 0);
INSERT INTO `igraci` VALUES(300, 'APA91bGl2fZNV2xLovME_g1-irr8WzgrJoulXcg_sytAPqHY0MI2h1tBbcNsJSG9h_60kZGweQeAhhkGr77NHVcNCONIi8DVYNHdjG3tGuiNhPVC7aWDleBHdYwVdybZhx7mii3nAy6B72QmOcNs5JzIartn3qcWsA', 146, 'L', '43.31693166666667', '21.894795', 0);
INSERT INTO `igraci` VALUES(301, 'APA91bGkmMvNe8nOH7XSx4ITcqqKg2boG6Hj4RYCpWd9B2-hpYxFBkon71A-cHr4qgW6cVEZZ3OCpdQqL9TmKqkj4T325syl4s7q_76XC4LgQ-Rho7kKpw7pQ8rifmB7W4B7GoX5PQt7qG816lilaEOMbwILIxm9NQ', 146, 'P', '0.0', '0.0', 0);
INSERT INTO `igraci` VALUES(302, 'APA91bGl2fZNV2xLovME_g1-irr8WzgrJoulXcg_sytAPqHY0MI2h1tBbcNsJSG9h_60kZGweQeAhhkGr77NHVcNCONIi8DVYNHdjG3tGuiNhPVC7aWDleBHdYwVdybZhx7mii3nAy6B72QmOcNs5JzIartn3qcWsA', 147, 'L', '0.0', '0.0', 0);
INSERT INTO `igraci` VALUES(303, 'APA91bGl2fZNV2xLovME_g1-irr8WzgrJoulXcg_sytAPqHY0MI2h1tBbcNsJSG9h_60kZGweQeAhhkGr77NHVcNCONIi8DVYNHdjG3tGuiNhPVC7aWDleBHdYwVdybZhx7mii3nAy6B72QmOcNs5JzIartn3qcWsA', 148, 'L', '0.0', '0.0', 0);
INSERT INTO `igraci` VALUES(304, 'APA91bGkmMvNe8nOH7XSx4ITcqqKg2boG6Hj4RYCpWd9B2-hpYxFBkon71A-cHr4qgW6cVEZZ3OCpdQqL9TmKqkj4T325syl4s7q_76XC4LgQ-Rho7kKpw7pQ8rifmB7W4B7GoX5PQt7qG816lilaEOMbwILIxm9NQ', 148, 'P', '0.0', '0.0', 0);
INSERT INTO `igraci` VALUES(305, 'APA91bGl2fZNV2xLovME_g1-irr8WzgrJoulXcg_sytAPqHY0MI2h1tBbcNsJSG9h_60kZGweQeAhhkGr77NHVcNCONIi8DVYNHdjG3tGuiNhPVC7aWDleBHdYwVdybZhx7mii3nAy6B72QmOcNs5JzIartn3qcWsA', 149, 'L', '43.318615', '21.89267166666667', 0);
INSERT INTO `igraci` VALUES(306, 'APA91bGkmMvNe8nOH7XSx4ITcqqKg2boG6Hj4RYCpWd9B2-hpYxFBkon71A-cHr4qgW6cVEZZ3OCpdQqL9TmKqkj4T325syl4s7q_76XC4LgQ-Rho7kKpw7pQ8rifmB7W4B7GoX5PQt7qG816lilaEOMbwILIxm9NQ', 149, 'P', '0.0', '0.0', 0);
INSERT INTO `igraci` VALUES(307, 'APA91bGIeOGDu32qmK8pIn8ifv-x3bnfzsHnARHOnUSwWOCpezAEevPrinYiDd40Hir8YVosxwrUqkJCh0BOhny_F6LgLMtxJKdQjnaHI7jnyskXvrQ-RnKnZajHnZf_y4vcU9ep8m6zHqSUuF9Mdzd3AC4G0jPyag', 150, 'L', '43.318615', '21.89267166666667', 0);
INSERT INTO `igraci` VALUES(308, 'APA91bG5wf6caQpku2bV_8y_WIELpV_8TInv0eL02vjyJbDwQXhHAg16KgA9cjAgI8pzb54RmVsR0Du14PF3YFd7paRuUzOKuekK3ZvubP25j-KDbCrT01cymKjRwizuv1nMWazepBC2PpVkxXS3839CK6oTaB9A2A', 150, 'P', '0.0', '0.0', 0);
INSERT INTO `igraci` VALUES(309, 'APA91bGfzhjS0Y65uMSHkls7M16OH1AN26bUKLX5L_1pA3wCNWp8NjFXHDrVcwN6NPmjVLKdaHuFzxH0ZZbxVL9gdOgpnhC8yqq3gILIrqWGQ-o9OQ1jz-_Oq6EQ3lo3Hsmbb4odA_oPpGbeaqIL-WifX639MXUAtw', 151, 'L', '43.318615', '21.89267166666667', 0);
INSERT INTO `igraci` VALUES(310, 'APA91bH-7wf4FC0MO9JruQlSau5iStH01FDu-yIGPsx0c5lujEgN8HJaRAKa9JXtkTk_7IboUQOP8UqDPNRHGEw0h51-CmetV3l0uPl9qDxUxQafUatP2R0syJDC0I9bioKT7vNLkbcj_meJz1Z6ZA0Y8wd4W7I3Wg', 151, 'P', '43.318615', '21.89267166666667', 0);
INSERT INTO `igraci` VALUES(311, 'APA91bH-7wf4FC0MO9JruQlSau5iStH01FDu-yIGPsx0c5lujEgN8HJaRAKa9JXtkTk_7IboUQOP8UqDPNRHGEw0h51-CmetV3l0uPl9qDxUxQafUatP2R0syJDC0I9bioKT7vNLkbcj_meJz1Z6ZA0Y8wd4W7I3Wg', 152, 'L', '43.318615', '21.89267166666667', 0);
INSERT INTO `igraci` VALUES(312, 'APA91bGnDIMmCPz59WlvFI5Jgl66g7eJ6V9gQ3hTpeVqlb7SIeuNn8itvmpTicLN0yjqoYa6JlQAI83TSG39O0u844KSFjbTnUIn9a2WXIttPRFv1pGQbBiHzAwNHFGu3F7RUvvMI2Nk2bEn3uKm9lQdI6fbxIKnQA', 152, 'P', '0.0', '0.0', 0);
INSERT INTO `igraci` VALUES(313, 'APA91bH-7wf4FC0MO9JruQlSau5iStH01FDu-yIGPsx0c5lujEgN8HJaRAKa9JXtkTk_7IboUQOP8UqDPNRHGEw0h51-CmetV3l0uPl9qDxUxQafUatP2R0syJDC0I9bioKT7vNLkbcj_meJz1Z6ZA0Y8wd4W7I3Wg', 153, 'L', '0.0', '0.0', 0);
INSERT INTO `igraci` VALUES(314, 'APA91bGnDIMmCPz59WlvFI5Jgl66g7eJ6V9gQ3hTpeVqlb7SIeuNn8itvmpTicLN0yjqoYa6JlQAI83TSG39O0u844KSFjbTnUIn9a2WXIttPRFv1pGQbBiHzAwNHFGu3F7RUvvMI2Nk2bEn3uKm9lQdI6fbxIKnQA', 153, 'P', '0.0', '0.0', 0);
INSERT INTO `igraci` VALUES(315, 'APA91bH-7wf4FC0MO9JruQlSau5iStH01FDu-yIGPsx0c5lujEgN8HJaRAKa9JXtkTk_7IboUQOP8UqDPNRHGEw0h51-CmetV3l0uPl9qDxUxQafUatP2R0syJDC0I9bioKT7vNLkbcj_meJz1Z6ZA0Y8wd4W7I3Wg', 154, 'L', '43.313948333333336', '21.89539', 0);
INSERT INTO `igraci` VALUES(316, 'APA91bGnDIMmCPz59WlvFI5Jgl66g7eJ6V9gQ3hTpeVqlb7SIeuNn8itvmpTicLN0yjqoYa6JlQAI83TSG39O0u844KSFjbTnUIn9a2WXIttPRFv1pGQbBiHzAwNHFGu3F7RUvvMI2Nk2bEn3uKm9lQdI6fbxIKnQA', 154, 'P', '0.0', '0.0', 0);
INSERT INTO `igraci` VALUES(317, 'APA91bGl2fZNV2xLovME_g1-irr8WzgrJoulXcg_sytAPqHY0MI2h1tBbcNsJSG9h_60kZGweQeAhhkGr77NHVcNCONIi8DVYNHdjG3tGuiNhPVC7aWDleBHdYwVdybZhx7mii3nAy6B72QmOcNs5JzIartn3qcWsA', 155, 'L', '43.132588333333324', '21.889986666666665', 0);
INSERT INTO `igraci` VALUES(318, 'APA91bGkmMvNe8nOH7XSx4ITcqqKg2boG6Hj4RYCpWd9B2-hpYxFBkon71A-cHr4qgW6cVEZZ3OCpdQqL9TmKqkj4T325syl4s7q_76XC4LgQ-Rho7kKpw7pQ8rifmB7W4B7GoX5PQt7qG816lilaEOMbwILIxm9NQ', 155, 'P', '43.13457833333334', '21.889986666666665', 0);
INSERT INTO `igraci` VALUES(319, 'APA91bGl2fZNV2xLovME_g1-irr8WzgrJoulXcg_sytAPqHY0MI2h1tBbcNsJSG9h_60kZGweQeAhhkGr77NHVcNCONIi8DVYNHdjG3tGuiNhPVC7aWDleBHdYwVdybZhx7mii3nAy6B72QmOcNs5JzIartn3qcWsA', 155, 'P', '43.132588333333324', '21.889986666666665', 0);
INSERT INTO `igraci` VALUES(320, 'APA91bEVCDvFjFQDZJd5xSdPqLTnDt4ogubgNeCPUF6VE_H1x4bRoFHATuyioYKhUbmyVRfOz4bB44H82dqMYTXx_k4rATWyzLRz8Ljz8tx09JfoQBwRckQl73WT7VG5GmzmpPumtbB9TOdVvBsxM8Wio_FdcZoL7Q', 156, 'L', '43.13879', '21.889986666666665', 0);
INSERT INTO `igraci` VALUES(293, 'APA91bFKrFpLg9QTO0Dhq6Du_wX_xRz1zBehqMrsqDmKBgwfV9SbXCqrLMrQHgrXEee3fWHa6tetIizjXGBDBTV_LYWIT4qgX-LPKxtit64vHctwriOcV_9mmluCPOUk9o1fVL3ur38CxlKT9Qk2EeqEPZUd7XoHqQ', 143, 'L', '43.31693166666667', '21.894795', 0);

-- --------------------------------------------------------

--
-- Table structure for table `igre`
--

CREATE TABLE `igre` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `ime` varchar(15) NOT NULL,
  `idMape` int(10) NOT NULL,
  `status` int(11) DEFAULT NULL,
  `vreme` time DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=163 ;

--
-- Dumping data for table `igre`
--

INSERT INTO `igre` VALUES(162, 'mmm5', 1, NULL, NULL);
INSERT INTO `igre` VALUES(161, 'mm3', 1, 1, NULL);
INSERT INTO `igre` VALUES(160, 'mm2', 1, 2, NULL);
INSERT INTO `igre` VALUES(159, 'mladenmladen', 1, 2, NULL);
INSERT INTO `igre` VALUES(158, 'kskkskkksk', 1, 1, NULL);
INSERT INTO `igre` VALUES(157, 'milena10', 1, 1, NULL);
INSERT INTO `igre` VALUES(156, 'cccvvvvbbb', 1, 1, NULL);
INSERT INTO `igre` VALUES(155, 'jjjjjjuuu', 1, NULL, NULL);
INSERT INTO `igre` VALUES(154, 'milena9', 1, 1, NULL);
INSERT INTO `igre` VALUES(153, 'milena8', 1, 1, NULL);
INSERT INTO `igre` VALUES(152, 'milena6', 1, NULL, NULL);
INSERT INTO `igre` VALUES(151, 'milena5', 1, NULL, NULL);
INSERT INTO `igre` VALUES(150, 'milena3', 1, NULL, NULL);
INSERT INTO `igre` VALUES(149, 'ml', 1, NULL, NULL);
INSERT INTO `igre` VALUES(148, 'mladen4', 1, NULL, NULL);
INSERT INTO `igre` VALUES(147, 'mladen3', 1, NULL, NULL);
INSERT INTO `igre` VALUES(146, 'mladen2', 1, NULL, NULL);
INSERT INTO `igre` VALUES(145, 'mladen', 1, NULL, NULL);
INSERT INTO `igre` VALUES(144, 'milena2', 1, NULL, NULL);
INSERT INTO `igre` VALUES(143, 'milena1', 1, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `mape`
--

CREATE TABLE `mape` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `latitude1` varchar(30) NOT NULL,
  `longitude1` varchar(30) NOT NULL,
  `latitude2` varchar(30) NOT NULL,
  `longitude2` varchar(30) NOT NULL,
  `latitude3` varchar(30) NOT NULL,
  `longitude3` varchar(30) NOT NULL,
  `latitude4` varchar(30) NOT NULL,
  `longitude4` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `mape`
--

INSERT INTO `mape` VALUES(1, '43.328664', '21.886868', '43.329288', '21.90764', '43.313615', '21.887126', '43.31362', '21.907833');

-- --------------------------------------------------------

--
-- Table structure for table `objekti`
--

CREATE TABLE `objekti` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `naziv` varchar(30) NOT NULL,
  `idMape` int(10) NOT NULL,
  `latitude` varchar(30) NOT NULL,
  `longitude` varchar(30) NOT NULL,
  `cena` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=8 ;

--
-- Dumping data for table `objekti`
--

INSERT INTO `objekti` VALUES(1, 'banka1', 1, '43.3186149597168', '21.892671585083008', '50 000');
INSERT INTO `objekti` VALUES(2, 'banka2', 1, '43.31394958496094', '21.8953914642334', '70 000');
INSERT INTO `objekti` VALUES(3, 'menjacnica1', 1, '43.31980895996094', '21.895347595214844', '100 000');
INSERT INTO `objekti` VALUES(4, 'menjacnica2', 1, '43.31504821777344', '21.89154815673828', '120 000');
INSERT INTO `objekti` VALUES(5, 'zlatara', 1, '43.316810607910156', '21.888870239257813', '500 000');
INSERT INTO `objekti` VALUES(6, 'sigurna kuca', 1, '43.31452941894531', '21.888486862182617', '0');
INSERT INTO `objekti` VALUES(7, 'policija', 1, '43.31875228881836', '21.90308380126953', '0');

-- --------------------------------------------------------

--
-- Table structure for table `objekti_statusi`
--

CREATE TABLE `objekti_statusi` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `idIgre` int(10) NOT NULL,
  `idObjekta` int(10) NOT NULL,
  `status` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1093 ;

--
-- Dumping data for table `objekti_statusi`
--

INSERT INTO `objekti_statusi` VALUES(1092, 162, 7, NULL);
INSERT INTO `objekti_statusi` VALUES(1091, 162, 6, NULL);
INSERT INTO `objekti_statusi` VALUES(1090, 162, 5, NULL);
INSERT INTO `objekti_statusi` VALUES(1089, 162, 4, NULL);
INSERT INTO `objekti_statusi` VALUES(1088, 162, 3, NULL);
INSERT INTO `objekti_statusi` VALUES(1087, 162, 2, NULL);
INSERT INTO `objekti_statusi` VALUES(1086, 162, 1, NULL);
INSERT INTO `objekti_statusi` VALUES(1085, 161, 7, NULL);
INSERT INTO `objekti_statusi` VALUES(1084, 161, 6, NULL);
INSERT INTO `objekti_statusi` VALUES(1083, 161, 5, NULL);
INSERT INTO `objekti_statusi` VALUES(1082, 161, 4, NULL);
INSERT INTO `objekti_statusi` VALUES(1081, 161, 3, NULL);
INSERT INTO `objekti_statusi` VALUES(1080, 161, 2, NULL);
INSERT INTO `objekti_statusi` VALUES(1079, 161, 1, NULL);
INSERT INTO `objekti_statusi` VALUES(1078, 160, 7, NULL);
INSERT INTO `objekti_statusi` VALUES(1077, 160, 6, NULL);
INSERT INTO `objekti_statusi` VALUES(1076, 160, 5, NULL);
INSERT INTO `objekti_statusi` VALUES(1075, 160, 4, NULL);
INSERT INTO `objekti_statusi` VALUES(1074, 160, 3, NULL);
INSERT INTO `objekti_statusi` VALUES(1073, 160, 2, NULL);
INSERT INTO `objekti_statusi` VALUES(1072, 160, 1, NULL);
INSERT INTO `objekti_statusi` VALUES(1071, 159, 7, NULL);
INSERT INTO `objekti_statusi` VALUES(1070, 159, 6, NULL);
INSERT INTO `objekti_statusi` VALUES(1069, 159, 5, NULL);
INSERT INTO `objekti_statusi` VALUES(1068, 159, 4, NULL);
INSERT INTO `objekti_statusi` VALUES(1067, 159, 3, NULL);
INSERT INTO `objekti_statusi` VALUES(1066, 159, 2, NULL);
INSERT INTO `objekti_statusi` VALUES(1065, 159, 1, NULL);
INSERT INTO `objekti_statusi` VALUES(1064, 158, 7, NULL);
INSERT INTO `objekti_statusi` VALUES(1063, 158, 6, NULL);
INSERT INTO `objekti_statusi` VALUES(1062, 158, 5, NULL);
INSERT INTO `objekti_statusi` VALUES(1061, 158, 4, NULL);
INSERT INTO `objekti_statusi` VALUES(1060, 158, 3, NULL);
INSERT INTO `objekti_statusi` VALUES(1059, 158, 2, NULL);
INSERT INTO `objekti_statusi` VALUES(1058, 158, 1, NULL);
INSERT INTO `objekti_statusi` VALUES(1057, 157, 7, NULL);
INSERT INTO `objekti_statusi` VALUES(1056, 157, 6, NULL);
INSERT INTO `objekti_statusi` VALUES(1055, 157, 5, '1');
INSERT INTO `objekti_statusi` VALUES(1054, 157, 4, NULL);
INSERT INTO `objekti_statusi` VALUES(1053, 157, 3, NULL);
INSERT INTO `objekti_statusi` VALUES(1052, 157, 2, NULL);
INSERT INTO `objekti_statusi` VALUES(1051, 157, 1, '1');
INSERT INTO `objekti_statusi` VALUES(1050, 156, 7, NULL);
INSERT INTO `objekti_statusi` VALUES(1049, 156, 6, NULL);
INSERT INTO `objekti_statusi` VALUES(1048, 156, 5, NULL);
INSERT INTO `objekti_statusi` VALUES(1047, 156, 4, NULL);
INSERT INTO `objekti_statusi` VALUES(1046, 156, 3, NULL);
INSERT INTO `objekti_statusi` VALUES(1045, 156, 2, NULL);
INSERT INTO `objekti_statusi` VALUES(1044, 156, 1, NULL);
INSERT INTO `objekti_statusi` VALUES(1043, 155, 7, NULL);
INSERT INTO `objekti_statusi` VALUES(1042, 155, 6, NULL);
INSERT INTO `objekti_statusi` VALUES(1041, 155, 5, NULL);
INSERT INTO `objekti_statusi` VALUES(1040, 155, 4, NULL);
INSERT INTO `objekti_statusi` VALUES(1039, 155, 3, NULL);
INSERT INTO `objekti_statusi` VALUES(1038, 155, 2, NULL);
INSERT INTO `objekti_statusi` VALUES(1037, 155, 1, NULL);
INSERT INTO `objekti_statusi` VALUES(1036, 154, 7, NULL);
INSERT INTO `objekti_statusi` VALUES(1035, 154, 6, NULL);
INSERT INTO `objekti_statusi` VALUES(1034, 154, 5, '1');
INSERT INTO `objekti_statusi` VALUES(1033, 154, 4, NULL);
INSERT INTO `objekti_statusi` VALUES(1032, 154, 3, '1');
INSERT INTO `objekti_statusi` VALUES(1031, 154, 2, '1');
INSERT INTO `objekti_statusi` VALUES(1030, 154, 1, '1');
INSERT INTO `objekti_statusi` VALUES(1029, 153, 7, NULL);
INSERT INTO `objekti_statusi` VALUES(1028, 153, 6, NULL);
INSERT INTO `objekti_statusi` VALUES(1027, 153, 5, NULL);
INSERT INTO `objekti_statusi` VALUES(1026, 153, 4, NULL);
INSERT INTO `objekti_statusi` VALUES(1025, 153, 3, NULL);
INSERT INTO `objekti_statusi` VALUES(1024, 153, 2, NULL);
INSERT INTO `objekti_statusi` VALUES(1023, 153, 1, NULL);
INSERT INTO `objekti_statusi` VALUES(1022, 152, 7, NULL);
INSERT INTO `objekti_statusi` VALUES(1021, 152, 6, NULL);
INSERT INTO `objekti_statusi` VALUES(1020, 152, 5, NULL);
INSERT INTO `objekti_statusi` VALUES(1019, 152, 4, NULL);
INSERT INTO `objekti_statusi` VALUES(1018, 152, 3, NULL);
INSERT INTO `objekti_statusi` VALUES(1017, 152, 2, NULL);
INSERT INTO `objekti_statusi` VALUES(1016, 152, 1, '1');
INSERT INTO `objekti_statusi` VALUES(1015, 151, 7, NULL);
INSERT INTO `objekti_statusi` VALUES(1014, 151, 6, NULL);
INSERT INTO `objekti_statusi` VALUES(1013, 151, 5, NULL);
INSERT INTO `objekti_statusi` VALUES(1012, 151, 4, NULL);
INSERT INTO `objekti_statusi` VALUES(1011, 151, 3, NULL);
INSERT INTO `objekti_statusi` VALUES(1010, 151, 2, NULL);
INSERT INTO `objekti_statusi` VALUES(1009, 151, 1, '1');
INSERT INTO `objekti_statusi` VALUES(1008, 150, 7, NULL);
INSERT INTO `objekti_statusi` VALUES(1007, 150, 6, NULL);
INSERT INTO `objekti_statusi` VALUES(1006, 150, 5, NULL);
INSERT INTO `objekti_statusi` VALUES(1005, 150, 4, NULL);
INSERT INTO `objekti_statusi` VALUES(1004, 150, 3, NULL);
INSERT INTO `objekti_statusi` VALUES(1003, 150, 2, NULL);
INSERT INTO `objekti_statusi` VALUES(1002, 150, 1, '1');
INSERT INTO `objekti_statusi` VALUES(1001, 149, 7, NULL);
INSERT INTO `objekti_statusi` VALUES(1000, 149, 6, NULL);
INSERT INTO `objekti_statusi` VALUES(999, 149, 5, NULL);
INSERT INTO `objekti_statusi` VALUES(998, 149, 4, NULL);
INSERT INTO `objekti_statusi` VALUES(997, 149, 3, NULL);
INSERT INTO `objekti_statusi` VALUES(996, 149, 2, NULL);
INSERT INTO `objekti_statusi` VALUES(995, 149, 1, '1');
INSERT INTO `objekti_statusi` VALUES(994, 148, 7, NULL);
INSERT INTO `objekti_statusi` VALUES(993, 148, 6, NULL);
INSERT INTO `objekti_statusi` VALUES(992, 148, 5, NULL);
INSERT INTO `objekti_statusi` VALUES(991, 148, 4, NULL);
INSERT INTO `objekti_statusi` VALUES(990, 148, 3, NULL);
INSERT INTO `objekti_statusi` VALUES(989, 148, 2, NULL);
INSERT INTO `objekti_statusi` VALUES(988, 148, 1, NULL);
INSERT INTO `objekti_statusi` VALUES(987, 147, 7, NULL);
INSERT INTO `objekti_statusi` VALUES(986, 147, 6, NULL);
INSERT INTO `objekti_statusi` VALUES(985, 147, 5, NULL);
INSERT INTO `objekti_statusi` VALUES(984, 147, 4, NULL);
INSERT INTO `objekti_statusi` VALUES(983, 147, 3, NULL);
INSERT INTO `objekti_statusi` VALUES(982, 147, 2, NULL);
INSERT INTO `objekti_statusi` VALUES(981, 147, 1, NULL);
INSERT INTO `objekti_statusi` VALUES(980, 146, 7, NULL);
INSERT INTO `objekti_statusi` VALUES(979, 146, 6, NULL);
INSERT INTO `objekti_statusi` VALUES(978, 146, 5, NULL);
INSERT INTO `objekti_statusi` VALUES(977, 146, 4, NULL);
INSERT INTO `objekti_statusi` VALUES(976, 146, 3, NULL);
INSERT INTO `objekti_statusi` VALUES(975, 146, 2, NULL);
INSERT INTO `objekti_statusi` VALUES(974, 146, 1, NULL);
INSERT INTO `objekti_statusi` VALUES(973, 145, 7, NULL);
INSERT INTO `objekti_statusi` VALUES(972, 145, 6, NULL);
INSERT INTO `objekti_statusi` VALUES(971, 145, 5, NULL);
INSERT INTO `objekti_statusi` VALUES(970, 145, 4, NULL);
INSERT INTO `objekti_statusi` VALUES(969, 145, 3, NULL);
INSERT INTO `objekti_statusi` VALUES(968, 145, 2, NULL);
INSERT INTO `objekti_statusi` VALUES(967, 145, 1, NULL);
INSERT INTO `objekti_statusi` VALUES(966, 144, 7, NULL);
INSERT INTO `objekti_statusi` VALUES(965, 144, 6, NULL);
INSERT INTO `objekti_statusi` VALUES(964, 144, 5, NULL);
INSERT INTO `objekti_statusi` VALUES(963, 144, 4, NULL);
INSERT INTO `objekti_statusi` VALUES(962, 144, 3, NULL);
INSERT INTO `objekti_statusi` VALUES(961, 144, 2, NULL);
INSERT INTO `objekti_statusi` VALUES(960, 144, 1, NULL);
INSERT INTO `objekti_statusi` VALUES(959, 143, 7, NULL);
INSERT INTO `objekti_statusi` VALUES(958, 143, 6, NULL);
INSERT INTO `objekti_statusi` VALUES(957, 143, 5, NULL);
INSERT INTO `objekti_statusi` VALUES(956, 143, 4, NULL);
INSERT INTO `objekti_statusi` VALUES(955, 143, 3, NULL);
INSERT INTO `objekti_statusi` VALUES(954, 143, 2, NULL);
INSERT INTO `objekti_statusi` VALUES(953, 143, 1, NULL);
INSERT INTO `objekti_statusi` VALUES(952, 142, 7, NULL);
INSERT INTO `objekti_statusi` VALUES(951, 142, 6, NULL);
INSERT INTO `objekti_statusi` VALUES(950, 142, 5, NULL);
INSERT INTO `objekti_statusi` VALUES(949, 142, 4, NULL);
INSERT INTO `objekti_statusi` VALUES(948, 142, 3, NULL);
INSERT INTO `objekti_statusi` VALUES(947, 142, 2, NULL);
INSERT INTO `objekti_statusi` VALUES(946, 142, 1, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `predmeti`
--

CREATE TABLE `predmeti` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `naziv` varchar(30) NOT NULL,
  `idMape` int(10) NOT NULL,
  `latitude` varchar(30) NOT NULL,
  `longitude` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=18 ;

--
-- Dumping data for table `predmeti`
--

INSERT INTO `predmeti` VALUES(1, 'luk i strela', 1, '43.316932678222656', '21.89479637145996');
INSERT INTO `predmeti` VALUES(2, 'kljuc', 1, '43.316776275634766', '21.89741325378418');
INSERT INTO `predmeti` VALUES(3, 'cekic', 1, '43.3111572265625', '21.891128540039063');
INSERT INTO `predmeti` VALUES(4, 'alat', 1, '43.312625885009766', '21.89287567138672');
INSERT INTO `predmeti` VALUES(5, 'lupa', 1, '43.31840133666992', '21.88938331604004');
INSERT INTO `predmeti` VALUES(6, 'okular', 1, '43.31379318237305', '21.9020156860351566');
INSERT INTO `predmeti` VALUES(7, 'kompjuter', 1, '43.3121223449707', '21.89866828918457');
INSERT INTO `predmeti` VALUES(8, 'sedativi', 1, '43.313873291015625', '21.897905349731445');
INSERT INTO `predmeti` VALUES(9, 'aparat za gasenje', 1, '43.314231872558594', '21.89449691772461');
INSERT INTO `predmeti` VALUES(10, 'pistolj', 1, '43.31474685668945', '21.90005111694336');
INSERT INTO `predmeti` VALUES(11, 'night vision', 1, '43.310855865478516', '21.8962764733999023');
INSERT INTO `predmeti` VALUES(12, 'puska', 1, '43.31143569946289', '21.898408889770508');
INSERT INTO `predmeti` VALUES(13, 'mobilni', 1, '43.31265640258789', '21.907039642333984');
INSERT INTO `predmeti` VALUES(14, 'fotoaparat', 1, '43.317626953125', '21.894872665405273');
INSERT INTO `predmeti` VALUES(15, 'dokument', 1, '43.315284729003906', '21.898143768310547');
INSERT INTO `predmeti` VALUES(16, 'pancir', 1, '43.30923080444336', '21.902814865112305');
INSERT INTO `predmeti` VALUES(17, 'ometac', 1, '43.316070556640625', '21.8998966217041');

-- --------------------------------------------------------

--
-- Table structure for table `predmeti_statusi`
--

CREATE TABLE `predmeti_statusi` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `idIgre` int(10) NOT NULL,
  `idPredmeta` int(10) NOT NULL,
  `status` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2653 ;

--
-- Dumping data for table `predmeti_statusi`
--

INSERT INTO `predmeti_statusi` VALUES(2652, 162, 17, NULL);
INSERT INTO `predmeti_statusi` VALUES(2651, 162, 16, NULL);
INSERT INTO `predmeti_statusi` VALUES(2650, 162, 15, NULL);
INSERT INTO `predmeti_statusi` VALUES(2649, 162, 14, NULL);
INSERT INTO `predmeti_statusi` VALUES(2648, 162, 13, NULL);
INSERT INTO `predmeti_statusi` VALUES(2647, 162, 12, NULL);
INSERT INTO `predmeti_statusi` VALUES(2646, 162, 11, NULL);
INSERT INTO `predmeti_statusi` VALUES(2645, 162, 10, NULL);
INSERT INTO `predmeti_statusi` VALUES(2644, 162, 9, NULL);
INSERT INTO `predmeti_statusi` VALUES(2643, 162, 8, NULL);
INSERT INTO `predmeti_statusi` VALUES(2642, 162, 7, NULL);
INSERT INTO `predmeti_statusi` VALUES(2641, 162, 6, NULL);
INSERT INTO `predmeti_statusi` VALUES(2640, 162, 5, NULL);
INSERT INTO `predmeti_statusi` VALUES(2639, 162, 4, NULL);
INSERT INTO `predmeti_statusi` VALUES(2638, 162, 3, NULL);
INSERT INTO `predmeti_statusi` VALUES(2637, 162, 2, NULL);
INSERT INTO `predmeti_statusi` VALUES(2636, 162, 1, NULL);
INSERT INTO `predmeti_statusi` VALUES(2635, 161, 17, NULL);
INSERT INTO `predmeti_statusi` VALUES(2634, 161, 16, NULL);
INSERT INTO `predmeti_statusi` VALUES(2633, 161, 15, NULL);
INSERT INTO `predmeti_statusi` VALUES(2632, 161, 14, NULL);
INSERT INTO `predmeti_statusi` VALUES(2631, 161, 13, NULL);
INSERT INTO `predmeti_statusi` VALUES(2630, 161, 12, NULL);
INSERT INTO `predmeti_statusi` VALUES(2629, 161, 11, NULL);
INSERT INTO `predmeti_statusi` VALUES(2628, 161, 10, NULL);
INSERT INTO `predmeti_statusi` VALUES(2627, 161, 9, NULL);
INSERT INTO `predmeti_statusi` VALUES(2626, 161, 8, NULL);
INSERT INTO `predmeti_statusi` VALUES(2625, 161, 7, NULL);
INSERT INTO `predmeti_statusi` VALUES(2624, 161, 6, NULL);
INSERT INTO `predmeti_statusi` VALUES(2623, 161, 5, NULL);
INSERT INTO `predmeti_statusi` VALUES(2622, 161, 4, NULL);
INSERT INTO `predmeti_statusi` VALUES(2621, 161, 3, NULL);
INSERT INTO `predmeti_statusi` VALUES(2620, 161, 2, NULL);
INSERT INTO `predmeti_statusi` VALUES(2619, 161, 1, NULL);
INSERT INTO `predmeti_statusi` VALUES(2618, 160, 17, NULL);
INSERT INTO `predmeti_statusi` VALUES(2617, 160, 16, NULL);
INSERT INTO `predmeti_statusi` VALUES(2616, 160, 15, NULL);
INSERT INTO `predmeti_statusi` VALUES(2615, 160, 14, NULL);
INSERT INTO `predmeti_statusi` VALUES(2614, 160, 13, NULL);
INSERT INTO `predmeti_statusi` VALUES(2613, 160, 12, NULL);
INSERT INTO `predmeti_statusi` VALUES(2612, 160, 11, NULL);
INSERT INTO `predmeti_statusi` VALUES(2611, 160, 10, NULL);
INSERT INTO `predmeti_statusi` VALUES(2610, 160, 9, NULL);
INSERT INTO `predmeti_statusi` VALUES(2609, 160, 8, NULL);
INSERT INTO `predmeti_statusi` VALUES(2608, 160, 7, NULL);
INSERT INTO `predmeti_statusi` VALUES(2607, 160, 6, NULL);
INSERT INTO `predmeti_statusi` VALUES(2606, 160, 5, NULL);
INSERT INTO `predmeti_statusi` VALUES(2605, 160, 4, NULL);
INSERT INTO `predmeti_statusi` VALUES(2604, 160, 3, NULL);
INSERT INTO `predmeti_statusi` VALUES(2603, 160, 2, NULL);
INSERT INTO `predmeti_statusi` VALUES(2602, 160, 1, NULL);
INSERT INTO `predmeti_statusi` VALUES(2601, 159, 17, NULL);
INSERT INTO `predmeti_statusi` VALUES(2600, 159, 16, NULL);
INSERT INTO `predmeti_statusi` VALUES(2599, 159, 15, NULL);
INSERT INTO `predmeti_statusi` VALUES(2598, 159, 14, NULL);
INSERT INTO `predmeti_statusi` VALUES(2597, 159, 13, NULL);
INSERT INTO `predmeti_statusi` VALUES(2596, 159, 12, NULL);
INSERT INTO `predmeti_statusi` VALUES(2595, 159, 11, NULL);
INSERT INTO `predmeti_statusi` VALUES(2594, 159, 10, NULL);
INSERT INTO `predmeti_statusi` VALUES(2593, 159, 9, NULL);
INSERT INTO `predmeti_statusi` VALUES(2592, 159, 8, NULL);
INSERT INTO `predmeti_statusi` VALUES(2591, 159, 7, NULL);
INSERT INTO `predmeti_statusi` VALUES(2590, 159, 6, NULL);
INSERT INTO `predmeti_statusi` VALUES(2589, 159, 5, NULL);
INSERT INTO `predmeti_statusi` VALUES(2588, 159, 4, NULL);
INSERT INTO `predmeti_statusi` VALUES(2587, 159, 3, NULL);
INSERT INTO `predmeti_statusi` VALUES(2586, 159, 2, NULL);
INSERT INTO `predmeti_statusi` VALUES(2585, 159, 1, NULL);
INSERT INTO `predmeti_statusi` VALUES(2584, 158, 17, NULL);
INSERT INTO `predmeti_statusi` VALUES(2583, 158, 16, NULL);
INSERT INTO `predmeti_statusi` VALUES(2582, 158, 15, NULL);
INSERT INTO `predmeti_statusi` VALUES(2581, 158, 14, NULL);
INSERT INTO `predmeti_statusi` VALUES(2580, 158, 13, NULL);
INSERT INTO `predmeti_statusi` VALUES(2579, 158, 12, NULL);
INSERT INTO `predmeti_statusi` VALUES(2578, 158, 11, NULL);
INSERT INTO `predmeti_statusi` VALUES(2577, 158, 10, NULL);
INSERT INTO `predmeti_statusi` VALUES(2576, 158, 9, NULL);
INSERT INTO `predmeti_statusi` VALUES(2575, 158, 8, NULL);
INSERT INTO `predmeti_statusi` VALUES(2574, 158, 7, NULL);
INSERT INTO `predmeti_statusi` VALUES(2573, 158, 6, NULL);
INSERT INTO `predmeti_statusi` VALUES(2572, 158, 5, NULL);
INSERT INTO `predmeti_statusi` VALUES(2571, 158, 4, NULL);
INSERT INTO `predmeti_statusi` VALUES(2570, 158, 3, NULL);
INSERT INTO `predmeti_statusi` VALUES(2569, 158, 2, NULL);
INSERT INTO `predmeti_statusi` VALUES(2568, 158, 1, NULL);
INSERT INTO `predmeti_statusi` VALUES(2567, 157, 17, NULL);
INSERT INTO `predmeti_statusi` VALUES(2566, 157, 16, NULL);
INSERT INTO `predmeti_statusi` VALUES(2565, 157, 15, NULL);
INSERT INTO `predmeti_statusi` VALUES(2564, 157, 14, NULL);
INSERT INTO `predmeti_statusi` VALUES(2563, 157, 13, NULL);
INSERT INTO `predmeti_statusi` VALUES(2562, 157, 12, NULL);
INSERT INTO `predmeti_statusi` VALUES(2561, 157, 11, NULL);
INSERT INTO `predmeti_statusi` VALUES(2560, 157, 10, NULL);
INSERT INTO `predmeti_statusi` VALUES(2559, 157, 9, NULL);
INSERT INTO `predmeti_statusi` VALUES(2558, 157, 8, NULL);
INSERT INTO `predmeti_statusi` VALUES(2557, 157, 7, NULL);
INSERT INTO `predmeti_statusi` VALUES(2556, 157, 6, NULL);
INSERT INTO `predmeti_statusi` VALUES(2555, 157, 5, NULL);
INSERT INTO `predmeti_statusi` VALUES(2554, 157, 4, NULL);
INSERT INTO `predmeti_statusi` VALUES(2553, 157, 3, '1');
INSERT INTO `predmeti_statusi` VALUES(2552, 157, 2, '1');
INSERT INTO `predmeti_statusi` VALUES(2551, 157, 1, '1');
INSERT INTO `predmeti_statusi` VALUES(2550, 156, 17, NULL);
INSERT INTO `predmeti_statusi` VALUES(2549, 156, 16, NULL);
INSERT INTO `predmeti_statusi` VALUES(2548, 156, 15, NULL);
INSERT INTO `predmeti_statusi` VALUES(2547, 156, 14, NULL);
INSERT INTO `predmeti_statusi` VALUES(2546, 156, 13, NULL);
INSERT INTO `predmeti_statusi` VALUES(2545, 156, 12, NULL);
INSERT INTO `predmeti_statusi` VALUES(2544, 156, 11, NULL);
INSERT INTO `predmeti_statusi` VALUES(2543, 156, 10, NULL);
INSERT INTO `predmeti_statusi` VALUES(2542, 156, 9, NULL);
INSERT INTO `predmeti_statusi` VALUES(2541, 156, 8, NULL);
INSERT INTO `predmeti_statusi` VALUES(2540, 156, 7, NULL);
INSERT INTO `predmeti_statusi` VALUES(2539, 156, 6, NULL);
INSERT INTO `predmeti_statusi` VALUES(2538, 156, 5, NULL);
INSERT INTO `predmeti_statusi` VALUES(2537, 156, 4, NULL);
INSERT INTO `predmeti_statusi` VALUES(2536, 156, 3, NULL);
INSERT INTO `predmeti_statusi` VALUES(2535, 156, 2, NULL);
INSERT INTO `predmeti_statusi` VALUES(2534, 156, 1, NULL);
INSERT INTO `predmeti_statusi` VALUES(2533, 155, 17, NULL);
INSERT INTO `predmeti_statusi` VALUES(2532, 155, 16, NULL);
INSERT INTO `predmeti_statusi` VALUES(2531, 155, 15, NULL);
INSERT INTO `predmeti_statusi` VALUES(2530, 155, 14, NULL);
INSERT INTO `predmeti_statusi` VALUES(2529, 155, 13, NULL);
INSERT INTO `predmeti_statusi` VALUES(2528, 155, 12, NULL);
INSERT INTO `predmeti_statusi` VALUES(2527, 155, 11, NULL);
INSERT INTO `predmeti_statusi` VALUES(2526, 155, 10, NULL);
INSERT INTO `predmeti_statusi` VALUES(2525, 155, 9, NULL);
INSERT INTO `predmeti_statusi` VALUES(2524, 155, 8, NULL);
INSERT INTO `predmeti_statusi` VALUES(2523, 155, 7, NULL);
INSERT INTO `predmeti_statusi` VALUES(2522, 155, 6, NULL);
INSERT INTO `predmeti_statusi` VALUES(2521, 155, 5, NULL);
INSERT INTO `predmeti_statusi` VALUES(2520, 155, 4, NULL);
INSERT INTO `predmeti_statusi` VALUES(2519, 155, 3, NULL);
INSERT INTO `predmeti_statusi` VALUES(2518, 155, 2, NULL);
INSERT INTO `predmeti_statusi` VALUES(2517, 155, 1, NULL);
INSERT INTO `predmeti_statusi` VALUES(2516, 154, 17, NULL);
INSERT INTO `predmeti_statusi` VALUES(2515, 154, 16, NULL);
INSERT INTO `predmeti_statusi` VALUES(2514, 154, 15, NULL);
INSERT INTO `predmeti_statusi` VALUES(2513, 154, 14, NULL);
INSERT INTO `predmeti_statusi` VALUES(2512, 154, 13, NULL);
INSERT INTO `predmeti_statusi` VALUES(2511, 154, 12, NULL);
INSERT INTO `predmeti_statusi` VALUES(2510, 154, 11, NULL);
INSERT INTO `predmeti_statusi` VALUES(2509, 154, 10, NULL);
INSERT INTO `predmeti_statusi` VALUES(2508, 154, 9, NULL);
INSERT INTO `predmeti_statusi` VALUES(2507, 154, 8, NULL);
INSERT INTO `predmeti_statusi` VALUES(2506, 154, 7, NULL);
INSERT INTO `predmeti_statusi` VALUES(2505, 154, 6, '1');
INSERT INTO `predmeti_statusi` VALUES(2504, 154, 5, '1');
INSERT INTO `predmeti_statusi` VALUES(2503, 154, 4, '1');
INSERT INTO `predmeti_statusi` VALUES(2502, 154, 3, '1');
INSERT INTO `predmeti_statusi` VALUES(2501, 154, 2, '1');
INSERT INTO `predmeti_statusi` VALUES(2500, 154, 1, '1');
INSERT INTO `predmeti_statusi` VALUES(2499, 153, 17, NULL);
INSERT INTO `predmeti_statusi` VALUES(2498, 153, 16, NULL);
INSERT INTO `predmeti_statusi` VALUES(2497, 153, 15, NULL);
INSERT INTO `predmeti_statusi` VALUES(2496, 153, 14, NULL);
INSERT INTO `predmeti_statusi` VALUES(2495, 153, 13, NULL);
INSERT INTO `predmeti_statusi` VALUES(2494, 153, 12, NULL);
INSERT INTO `predmeti_statusi` VALUES(2493, 153, 11, NULL);
INSERT INTO `predmeti_statusi` VALUES(2492, 153, 10, NULL);
INSERT INTO `predmeti_statusi` VALUES(2491, 153, 9, NULL);
INSERT INTO `predmeti_statusi` VALUES(2490, 153, 8, NULL);
INSERT INTO `predmeti_statusi` VALUES(2489, 153, 7, NULL);
INSERT INTO `predmeti_statusi` VALUES(2488, 153, 6, NULL);
INSERT INTO `predmeti_statusi` VALUES(2487, 153, 5, NULL);
INSERT INTO `predmeti_statusi` VALUES(2486, 153, 4, NULL);
INSERT INTO `predmeti_statusi` VALUES(2485, 153, 3, NULL);
INSERT INTO `predmeti_statusi` VALUES(2484, 153, 2, NULL);
INSERT INTO `predmeti_statusi` VALUES(2483, 153, 1, NULL);
INSERT INTO `predmeti_statusi` VALUES(2482, 152, 17, NULL);
INSERT INTO `predmeti_statusi` VALUES(2481, 152, 16, NULL);
INSERT INTO `predmeti_statusi` VALUES(2480, 152, 15, NULL);
INSERT INTO `predmeti_statusi` VALUES(2479, 152, 14, NULL);
INSERT INTO `predmeti_statusi` VALUES(2478, 152, 13, NULL);
INSERT INTO `predmeti_statusi` VALUES(2477, 152, 12, NULL);
INSERT INTO `predmeti_statusi` VALUES(2476, 152, 11, NULL);
INSERT INTO `predmeti_statusi` VALUES(2475, 152, 10, NULL);
INSERT INTO `predmeti_statusi` VALUES(2474, 152, 9, NULL);
INSERT INTO `predmeti_statusi` VALUES(2473, 152, 8, NULL);
INSERT INTO `predmeti_statusi` VALUES(2472, 152, 7, NULL);
INSERT INTO `predmeti_statusi` VALUES(2471, 152, 6, NULL);
INSERT INTO `predmeti_statusi` VALUES(2470, 152, 5, NULL);
INSERT INTO `predmeti_statusi` VALUES(2469, 152, 4, NULL);
INSERT INTO `predmeti_statusi` VALUES(2468, 152, 3, '1');
INSERT INTO `predmeti_statusi` VALUES(2467, 152, 2, '1');
INSERT INTO `predmeti_statusi` VALUES(2466, 152, 1, '1');
INSERT INTO `predmeti_statusi` VALUES(2465, 151, 17, NULL);
INSERT INTO `predmeti_statusi` VALUES(2464, 151, 16, NULL);
INSERT INTO `predmeti_statusi` VALUES(2463, 151, 15, NULL);
INSERT INTO `predmeti_statusi` VALUES(2462, 151, 14, NULL);
INSERT INTO `predmeti_statusi` VALUES(2461, 151, 13, NULL);
INSERT INTO `predmeti_statusi` VALUES(2460, 151, 12, NULL);
INSERT INTO `predmeti_statusi` VALUES(2459, 151, 11, NULL);
INSERT INTO `predmeti_statusi` VALUES(2458, 151, 10, NULL);
INSERT INTO `predmeti_statusi` VALUES(2457, 151, 9, NULL);
INSERT INTO `predmeti_statusi` VALUES(2456, 151, 8, NULL);
INSERT INTO `predmeti_statusi` VALUES(2455, 151, 7, NULL);
INSERT INTO `predmeti_statusi` VALUES(2454, 151, 6, NULL);
INSERT INTO `predmeti_statusi` VALUES(2453, 151, 5, NULL);
INSERT INTO `predmeti_statusi` VALUES(2452, 151, 4, NULL);
INSERT INTO `predmeti_statusi` VALUES(2451, 151, 3, '1');
INSERT INTO `predmeti_statusi` VALUES(2450, 151, 2, '1');
INSERT INTO `predmeti_statusi` VALUES(2449, 151, 1, '1');
INSERT INTO `predmeti_statusi` VALUES(2448, 150, 17, NULL);
INSERT INTO `predmeti_statusi` VALUES(2447, 150, 16, NULL);
INSERT INTO `predmeti_statusi` VALUES(2446, 150, 15, NULL);
INSERT INTO `predmeti_statusi` VALUES(2445, 150, 14, NULL);
INSERT INTO `predmeti_statusi` VALUES(2444, 150, 13, NULL);
INSERT INTO `predmeti_statusi` VALUES(2443, 150, 12, NULL);
INSERT INTO `predmeti_statusi` VALUES(2442, 150, 11, NULL);
INSERT INTO `predmeti_statusi` VALUES(2441, 150, 10, NULL);
INSERT INTO `predmeti_statusi` VALUES(2440, 150, 9, NULL);
INSERT INTO `predmeti_statusi` VALUES(2439, 150, 8, NULL);
INSERT INTO `predmeti_statusi` VALUES(2438, 150, 7, NULL);
INSERT INTO `predmeti_statusi` VALUES(2437, 150, 6, NULL);
INSERT INTO `predmeti_statusi` VALUES(2436, 150, 5, NULL);
INSERT INTO `predmeti_statusi` VALUES(2435, 150, 4, NULL);
INSERT INTO `predmeti_statusi` VALUES(2434, 150, 3, '1');
INSERT INTO `predmeti_statusi` VALUES(2433, 150, 2, '1');
INSERT INTO `predmeti_statusi` VALUES(2432, 150, 1, '1');
INSERT INTO `predmeti_statusi` VALUES(2431, 149, 17, NULL);
INSERT INTO `predmeti_statusi` VALUES(2430, 149, 16, NULL);
INSERT INTO `predmeti_statusi` VALUES(2429, 149, 15, NULL);
INSERT INTO `predmeti_statusi` VALUES(2428, 149, 14, NULL);
INSERT INTO `predmeti_statusi` VALUES(2427, 149, 13, NULL);
INSERT INTO `predmeti_statusi` VALUES(2426, 149, 12, NULL);
INSERT INTO `predmeti_statusi` VALUES(2425, 149, 11, NULL);
INSERT INTO `predmeti_statusi` VALUES(2424, 149, 10, NULL);
INSERT INTO `predmeti_statusi` VALUES(2423, 149, 9, NULL);
INSERT INTO `predmeti_statusi` VALUES(2422, 149, 8, NULL);
INSERT INTO `predmeti_statusi` VALUES(2421, 149, 7, NULL);
INSERT INTO `predmeti_statusi` VALUES(2420, 149, 6, NULL);
INSERT INTO `predmeti_statusi` VALUES(2419, 149, 5, NULL);
INSERT INTO `predmeti_statusi` VALUES(2418, 149, 4, NULL);
INSERT INTO `predmeti_statusi` VALUES(2417, 149, 3, '1');
INSERT INTO `predmeti_statusi` VALUES(2416, 149, 2, '1');
INSERT INTO `predmeti_statusi` VALUES(2415, 149, 1, '1');
INSERT INTO `predmeti_statusi` VALUES(2414, 148, 17, NULL);
INSERT INTO `predmeti_statusi` VALUES(2413, 148, 16, NULL);
INSERT INTO `predmeti_statusi` VALUES(2412, 148, 15, NULL);
INSERT INTO `predmeti_statusi` VALUES(2411, 148, 14, NULL);
INSERT INTO `predmeti_statusi` VALUES(2410, 148, 13, NULL);
INSERT INTO `predmeti_statusi` VALUES(2409, 148, 12, NULL);
INSERT INTO `predmeti_statusi` VALUES(2408, 148, 11, NULL);
INSERT INTO `predmeti_statusi` VALUES(2407, 148, 10, NULL);
INSERT INTO `predmeti_statusi` VALUES(2406, 148, 9, NULL);
INSERT INTO `predmeti_statusi` VALUES(2405, 148, 8, NULL);
INSERT INTO `predmeti_statusi` VALUES(2404, 148, 7, NULL);
INSERT INTO `predmeti_statusi` VALUES(2403, 148, 6, NULL);
INSERT INTO `predmeti_statusi` VALUES(2402, 148, 5, NULL);
INSERT INTO `predmeti_statusi` VALUES(2401, 148, 4, NULL);
INSERT INTO `predmeti_statusi` VALUES(2400, 148, 3, NULL);
INSERT INTO `predmeti_statusi` VALUES(2399, 148, 2, NULL);
INSERT INTO `predmeti_statusi` VALUES(2398, 148, 1, NULL);
INSERT INTO `predmeti_statusi` VALUES(2397, 147, 17, NULL);
INSERT INTO `predmeti_statusi` VALUES(2396, 147, 16, NULL);
INSERT INTO `predmeti_statusi` VALUES(2395, 147, 15, NULL);
INSERT INTO `predmeti_statusi` VALUES(2394, 147, 14, NULL);
INSERT INTO `predmeti_statusi` VALUES(2393, 147, 13, NULL);
INSERT INTO `predmeti_statusi` VALUES(2392, 147, 12, NULL);
INSERT INTO `predmeti_statusi` VALUES(2391, 147, 11, NULL);
INSERT INTO `predmeti_statusi` VALUES(2390, 147, 10, NULL);
INSERT INTO `predmeti_statusi` VALUES(2389, 147, 9, NULL);
INSERT INTO `predmeti_statusi` VALUES(2388, 147, 8, NULL);
INSERT INTO `predmeti_statusi` VALUES(2387, 147, 7, NULL);
INSERT INTO `predmeti_statusi` VALUES(2386, 147, 6, NULL);
INSERT INTO `predmeti_statusi` VALUES(2385, 147, 5, NULL);
INSERT INTO `predmeti_statusi` VALUES(2384, 147, 4, NULL);
INSERT INTO `predmeti_statusi` VALUES(2383, 147, 3, NULL);
INSERT INTO `predmeti_statusi` VALUES(2382, 147, 2, NULL);
INSERT INTO `predmeti_statusi` VALUES(2381, 147, 1, NULL);
INSERT INTO `predmeti_statusi` VALUES(2380, 146, 17, NULL);
INSERT INTO `predmeti_statusi` VALUES(2379, 146, 16, NULL);
INSERT INTO `predmeti_statusi` VALUES(2378, 146, 15, NULL);
INSERT INTO `predmeti_statusi` VALUES(2377, 146, 14, NULL);
INSERT INTO `predmeti_statusi` VALUES(2376, 146, 13, NULL);
INSERT INTO `predmeti_statusi` VALUES(2375, 146, 12, NULL);
INSERT INTO `predmeti_statusi` VALUES(2374, 146, 11, NULL);
INSERT INTO `predmeti_statusi` VALUES(2373, 146, 10, NULL);
INSERT INTO `predmeti_statusi` VALUES(2372, 146, 9, NULL);
INSERT INTO `predmeti_statusi` VALUES(2371, 146, 8, NULL);
INSERT INTO `predmeti_statusi` VALUES(2370, 146, 7, NULL);
INSERT INTO `predmeti_statusi` VALUES(2369, 146, 6, NULL);
INSERT INTO `predmeti_statusi` VALUES(2368, 146, 5, NULL);
INSERT INTO `predmeti_statusi` VALUES(2367, 146, 4, NULL);
INSERT INTO `predmeti_statusi` VALUES(2366, 146, 3, NULL);
INSERT INTO `predmeti_statusi` VALUES(2365, 146, 2, '1');
INSERT INTO `predmeti_statusi` VALUES(2364, 146, 1, '1');
INSERT INTO `predmeti_statusi` VALUES(2363, 145, 17, NULL);
INSERT INTO `predmeti_statusi` VALUES(2362, 145, 16, NULL);
INSERT INTO `predmeti_statusi` VALUES(2361, 145, 15, NULL);
INSERT INTO `predmeti_statusi` VALUES(2360, 145, 14, NULL);
INSERT INTO `predmeti_statusi` VALUES(2359, 145, 13, NULL);
INSERT INTO `predmeti_statusi` VALUES(2358, 145, 12, NULL);
INSERT INTO `predmeti_statusi` VALUES(2357, 145, 11, NULL);
INSERT INTO `predmeti_statusi` VALUES(2356, 145, 10, NULL);
INSERT INTO `predmeti_statusi` VALUES(2355, 145, 9, NULL);
INSERT INTO `predmeti_statusi` VALUES(2354, 145, 8, NULL);
INSERT INTO `predmeti_statusi` VALUES(2353, 145, 7, NULL);
INSERT INTO `predmeti_statusi` VALUES(2352, 145, 6, NULL);
INSERT INTO `predmeti_statusi` VALUES(2351, 145, 5, NULL);
INSERT INTO `predmeti_statusi` VALUES(2350, 145, 4, NULL);
INSERT INTO `predmeti_statusi` VALUES(2349, 145, 3, NULL);
INSERT INTO `predmeti_statusi` VALUES(2348, 145, 2, NULL);
INSERT INTO `predmeti_statusi` VALUES(2347, 145, 1, NULL);
INSERT INTO `predmeti_statusi` VALUES(2346, 144, 17, NULL);
INSERT INTO `predmeti_statusi` VALUES(2345, 144, 16, NULL);
INSERT INTO `predmeti_statusi` VALUES(2344, 144, 15, NULL);
INSERT INTO `predmeti_statusi` VALUES(2343, 144, 14, NULL);
INSERT INTO `predmeti_statusi` VALUES(2342, 144, 13, NULL);
INSERT INTO `predmeti_statusi` VALUES(2341, 144, 12, NULL);
INSERT INTO `predmeti_statusi` VALUES(2340, 144, 11, NULL);
INSERT INTO `predmeti_statusi` VALUES(2339, 144, 10, NULL);
INSERT INTO `predmeti_statusi` VALUES(2338, 144, 9, NULL);
INSERT INTO `predmeti_statusi` VALUES(2337, 144, 8, NULL);
INSERT INTO `predmeti_statusi` VALUES(2336, 144, 7, NULL);
INSERT INTO `predmeti_statusi` VALUES(2335, 144, 6, NULL);
INSERT INTO `predmeti_statusi` VALUES(2334, 144, 5, NULL);
INSERT INTO `predmeti_statusi` VALUES(2333, 144, 4, NULL);
INSERT INTO `predmeti_statusi` VALUES(2332, 144, 3, NULL);
INSERT INTO `predmeti_statusi` VALUES(2331, 144, 2, NULL);
INSERT INTO `predmeti_statusi` VALUES(2330, 144, 1, '1');
INSERT INTO `predmeti_statusi` VALUES(2329, 143, 17, NULL);
INSERT INTO `predmeti_statusi` VALUES(2328, 143, 16, NULL);
INSERT INTO `predmeti_statusi` VALUES(2327, 143, 15, NULL);
INSERT INTO `predmeti_statusi` VALUES(2326, 143, 14, NULL);
INSERT INTO `predmeti_statusi` VALUES(2325, 143, 13, NULL);
INSERT INTO `predmeti_statusi` VALUES(2324, 143, 12, NULL);
INSERT INTO `predmeti_statusi` VALUES(2323, 143, 11, NULL);
INSERT INTO `predmeti_statusi` VALUES(2322, 143, 10, NULL);
INSERT INTO `predmeti_statusi` VALUES(2321, 143, 9, NULL);
INSERT INTO `predmeti_statusi` VALUES(2320, 143, 8, NULL);
INSERT INTO `predmeti_statusi` VALUES(2319, 143, 7, NULL);
INSERT INTO `predmeti_statusi` VALUES(2318, 143, 6, NULL);
INSERT INTO `predmeti_statusi` VALUES(2317, 143, 5, NULL);
INSERT INTO `predmeti_statusi` VALUES(2316, 143, 4, NULL);
INSERT INTO `predmeti_statusi` VALUES(2315, 143, 3, NULL);
INSERT INTO `predmeti_statusi` VALUES(2314, 143, 2, NULL);
INSERT INTO `predmeti_statusi` VALUES(2313, 143, 1, NULL);
INSERT INTO `predmeti_statusi` VALUES(2312, 142, 17, NULL);
INSERT INTO `predmeti_statusi` VALUES(2311, 142, 16, NULL);
INSERT INTO `predmeti_statusi` VALUES(2310, 142, 15, NULL);
INSERT INTO `predmeti_statusi` VALUES(2309, 142, 14, NULL);
INSERT INTO `predmeti_statusi` VALUES(2308, 142, 13, NULL);
INSERT INTO `predmeti_statusi` VALUES(2307, 142, 12, NULL);
INSERT INTO `predmeti_statusi` VALUES(2306, 142, 11, NULL);
INSERT INTO `predmeti_statusi` VALUES(2305, 142, 10, NULL);
INSERT INTO `predmeti_statusi` VALUES(2304, 142, 9, NULL);
INSERT INTO `predmeti_statusi` VALUES(2303, 142, 8, NULL);
INSERT INTO `predmeti_statusi` VALUES(2302, 142, 7, NULL);
INSERT INTO `predmeti_statusi` VALUES(2301, 142, 6, NULL);
INSERT INTO `predmeti_statusi` VALUES(2300, 142, 5, NULL);
INSERT INTO `predmeti_statusi` VALUES(2299, 142, 4, NULL);
INSERT INTO `predmeti_statusi` VALUES(2298, 142, 3, NULL);
INSERT INTO `predmeti_statusi` VALUES(2297, 142, 2, NULL);
INSERT INTO `predmeti_statusi` VALUES(2296, 142, 1, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `uslovi_pljacke`
--

CREATE TABLE `uslovi_pljacke` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `idObjekta` int(10) NOT NULL,
  `idPredmeta` int(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=16 ;

--
-- Dumping data for table `uslovi_pljacke`
--

INSERT INTO `uslovi_pljacke` VALUES(1, 1, 1);
INSERT INTO `uslovi_pljacke` VALUES(2, 1, 2);
INSERT INTO `uslovi_pljacke` VALUES(3, 1, 3);
INSERT INTO `uslovi_pljacke` VALUES(4, 2, 4);
INSERT INTO `uslovi_pljacke` VALUES(5, 2, 5);
INSERT INTO `uslovi_pljacke` VALUES(6, 2, 6);
INSERT INTO `uslovi_pljacke` VALUES(7, 3, 7);
INSERT INTO `uslovi_pljacke` VALUES(8, 3, 8);
INSERT INTO `uslovi_pljacke` VALUES(9, 3, 9);
INSERT INTO `uslovi_pljacke` VALUES(10, 4, 10);
INSERT INTO `uslovi_pljacke` VALUES(11, 4, 11);
INSERT INTO `uslovi_pljacke` VALUES(12, 4, 12);
INSERT INTO `uslovi_pljacke` VALUES(13, 5, 13);
INSERT INTO `uslovi_pljacke` VALUES(14, 5, 14);
INSERT INTO `uslovi_pljacke` VALUES(15, 5, 15);
