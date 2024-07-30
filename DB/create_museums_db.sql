
USE [museums]
GO
/****** Object:  Table [dbo].[Museum]     ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Museum](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](300) NOT NULL,
	[Description] [text] NULL,
	[Address] [varchar](300) NULL,
	[TimeOpening] [time](7) NULL,
	[TimeClosing] [time](7) NULL,
 CONSTRAINT [PK_Museum_1] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[ArtObject]     ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[ArtObject](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](128) NULL,
	[Description] [text] NULL,
	[CreationYear] [smallint] NULL,
	[Artist] [varchar](200) NULL,
	[MuseumId] [int] NULL,
	[photo] [varbinary](max) NULL,
 CONSTRAINT [PK_ArtObject] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[Exhibition]     ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Exhibition](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](161) NOT NULL,
	[Description] [text] NULL,
	[MuseumId] [int] NOT NULL,
	[Started] [date] NULL,
	[Ended] [date] NULL,
 CONSTRAINT [PK_Exhibition] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[ArtObjectOnExhibition]     ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ArtObjectOnExhibition](
	[ExhibitionId] [int] NOT NULL,
	[ArtObjectId] [int] NOT NULL,
 CONSTRAINT [PK_ArtObjectOnExhibition] PRIMARY KEY CLUSTERED 
(
	[ExhibitionId] ASC,
	[ArtObjectId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  View [dbo].[MuseumWithExhibitionCount]     ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE view [dbo].[MuseumWithExhibitionCount] as 
SELECT museum.*, (SELECT COUNT(*) FROM exhibition WHERE exhibition.museumid = museum.id) as ExhibitionCount FROM museum
GO
/****** Object:  StoredProcedure [dbo].[UnbindArtObjectFromExhibition]     ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[UnbindArtObjectFromExhibition]
    @ArtObjectId INT,
    @ExhibitionId INT
AS
BEGIN
    DELETE FROM ArtObjectOnExhibition
    WHERE ArtObjectId = @ArtObjectId AND ExhibitionId = @ExhibitionId;
END;
GO
/****** Object:  UserDefinedFunction [dbo].[GetExhibitionsCountByArtObjectId]     ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE FUNCTION [dbo].[GetExhibitionsCountByArtObjectId]
    (@ArtObjectId INT)
RETURNS INT
AS
BEGIN
    DECLARE @ExhibitionsCount INT;

    SELECT @ExhibitionsCount = COUNT(*)
    FROM ArtObjectOnExhibition
    WHERE ArtObjectId = @ArtObjectId;

    RETURN @ExhibitionsCount;
END;
GO
/****** Object:  UserDefinedFunction [dbo].[GetExhibitionsByArtObjectId]     ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE FUNCTION [dbo].[GetExhibitionsByArtObjectId]
    (@ArtObjectId INT)
RETURNS TABLE
AS
RETURN
(
    SELECT e.*
    FROM Exhibition e
    INNER JOIN ArtObjectOnExhibition aoe ON e.Id = aoe.ExhibitionId
    WHERE aoe.ArtObjectId = @ArtObjectId
);
GO
/****** Object:  StoredProcedure [dbo].[AttachArtObjectFromExhibition]     ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[AttachArtObjectFromExhibition]
    @ArtObjectId INT,
    @ExhibitionId INT
AS
BEGIN
    INSERT INTO ArtObjectOnExhibition (ExhibitionId, ArtObjectId)
	VALUES (@ExhibitionId, @ArtObjectId);
END;
GO
/****** Object:  Default [DF__Museum__Name__1BFD2C07]     ******/
ALTER TABLE [dbo].[Museum] ADD  CONSTRAINT [DF__Museum__Name__1BFD2C07]  DEFAULT (NULL) FOR [Name]
GO
/****** Object:  Default [DF__Museum__Address__1CF15040]     ******/
ALTER TABLE [dbo].[Museum] ADD  CONSTRAINT [DF__Museum__Address__1CF15040]  DEFAULT (NULL) FOR [Address]
GO
/****** Object:  Default [DF__Museum__TimeOpen__1DE57479]     ******/
ALTER TABLE [dbo].[Museum] ADD  CONSTRAINT [DF__Museum__TimeOpen__1DE57479]  DEFAULT (NULL) FOR [TimeOpening]
GO
/****** Object:  Default [DF__Museum__TimeClos__1ED998B2]     ******/
ALTER TABLE [dbo].[Museum] ADD  CONSTRAINT [DF__Museum__TimeClos__1ED998B2]  DEFAULT (NULL) FOR [TimeClosing]
GO
/****** Object:  Default [DF__ArtObject__Name__060DEAE8]     ******/
ALTER TABLE [dbo].[ArtObject] ADD  CONSTRAINT [DF__ArtObject__Name__060DEAE8]  DEFAULT (NULL) FOR [Name]
GO
/****** Object:  Default [DF__ArtObject__Creat__07020F21]     ******/
ALTER TABLE [dbo].[ArtObject] ADD  CONSTRAINT [DF__ArtObject__Creat__07020F21]  DEFAULT (NULL) FOR [CreationYear]
GO
/****** Object:  Default [DF__ArtObject__Artis__07F6335A]     ******/
ALTER TABLE [dbo].[ArtObject] ADD  CONSTRAINT [DF__ArtObject__Artis__07F6335A]  DEFAULT (NULL) FOR [Artist]
GO
/****** Object:  Default [DF__ArtObject__Museu__08EA5793]     ******/
ALTER TABLE [dbo].[ArtObject] ADD  CONSTRAINT [DF__ArtObject__Museu__08EA5793]  DEFAULT (NULL) FOR [MuseumId]
GO
/****** Object:  Default [DF__Exhibition__Name__164452B1]     ******/
ALTER TABLE [dbo].[Exhibition] ADD  CONSTRAINT [DF__Exhibition__Name__164452B1]  DEFAULT (NULL) FOR [Name]
GO
/****** Object:  Default [DF__Exhibitio__Museu__1920BF5C]     ******/
ALTER TABLE [dbo].[Exhibition] ADD  CONSTRAINT [DF__Exhibitio__Museu__1920BF5C]  DEFAULT (NULL) FOR [MuseumId]
GO
/****** Object:  Default [DF__ArtObject__Excib__0AD2A005]     ******/
ALTER TABLE [dbo].[ArtObjectOnExhibition] ADD  CONSTRAINT [DF__ArtObject__Excib__0AD2A005]  DEFAULT (NULL) FOR [ExhibitionId]
GO
/****** Object:  Default [DF__ArtObject__ArtOb__0BC6C43E]     ******/
ALTER TABLE [dbo].[ArtObjectOnExhibition] ADD  CONSTRAINT [DF__ArtObject__ArtOb__0BC6C43E]  DEFAULT (NULL) FOR [ArtObjectId]
GO
/****** Object:  ForeignKey [FK_ArtObject_Museum]     ******/
ALTER TABLE [dbo].[ArtObject]  WITH CHECK ADD  CONSTRAINT [FK_ArtObject_Museum] FOREIGN KEY([MuseumId])
REFERENCES [dbo].[Museum] ([Id])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[ArtObject] CHECK CONSTRAINT [FK_ArtObject_Museum]
GO
/****** Object:  ForeignKey [FK_Exhibition_Museum]     ******/
ALTER TABLE [dbo].[Exhibition]  WITH CHECK ADD  CONSTRAINT [FK_Exhibition_Museum] FOREIGN KEY([MuseumId])
REFERENCES [dbo].[Museum] ([Id])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[Exhibition] CHECK CONSTRAINT [FK_Exhibition_Museum]
GO
/****** Object:  ForeignKey [FK_ArtObjectOnExhibition_ArtObject]     ******/
ALTER TABLE [dbo].[ArtObjectOnExhibition]  WITH CHECK ADD  CONSTRAINT [FK_ArtObjectOnExhibition_ArtObject] FOREIGN KEY([ArtObjectId])
REFERENCES [dbo].[ArtObject] ([Id])
GO
ALTER TABLE [dbo].[ArtObjectOnExhibition] CHECK CONSTRAINT [FK_ArtObjectOnExhibition_ArtObject]
GO
/****** Object:  ForeignKey [FK_ArtObjectOnExhibition_Exhibition]     ******/
ALTER TABLE [dbo].[ArtObjectOnExhibition]  WITH CHECK ADD  CONSTRAINT [FK_ArtObjectOnExhibition_Exhibition] FOREIGN KEY([ExhibitionId])
REFERENCES [dbo].[Exhibition] ([Id])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[ArtObjectOnExhibition] CHECK CONSTRAINT [FK_ArtObjectOnExhibition_Exhibition]
GO
