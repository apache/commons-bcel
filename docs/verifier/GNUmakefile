#    JustIce, the class file verifier for use with BCEL was written
#    in 2001 by Enver Haase; (c) 2001 Enver Haase.
#
#    This program is free software; you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation; either version 2 of the License, or
#    (at your option) any later version.
#
#    This program is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU General Public License for more details.
#
#    You should have received a copy of the GNU General Public License
#    along with this program; if not, write to the Free Software
#    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
#
# $Id$

RM := rm

all:	psgz

distclean:	clean
	$(RM) -rf *~

ps:
	lyx -e ps JustIce.lyx

pdf:	ps
	ps2pdf JustIce.ps JustIce.pdf

psgz:	ps
	gzip JustIce.ps

clean:
	$(RM) -rf JustIce.ps JustIce.pdf JustIce.ps.gz
